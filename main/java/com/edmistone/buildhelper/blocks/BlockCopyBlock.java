package com.edmistone.buildhelper.blocks;

import com.edmistone.buildhelper.Info;
import com.edmistone.buildhelper.helpers.TagHelper;
import com.edmistone.buildhelper.operations.Chat;
import com.edmistone.buildhelper.operations.Compare;
import com.edmistone.buildhelper.operations.Rotate;
import com.edmistone.buildhelper.registry.Items;
import com.edmistone.buildhelper.registry.Sounds;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/** Copy Block can be placed on 2 sides (in a diagonal line) of a section to be copied/rotated.
 *  Sections can be pasted using a paste block or rotated by using build tool on the copy block.
 *  @author Aaron Edmistone */
public class BlockCopyBlock extends Block
{
	public BlockCopyBlock(String unlocalizedName)
	{		
		super(Properties.create(Material.GROUND).hardnessAndResistance(1, 20));
		this.setRegistryName(new ResourceLocation(Info.MODID, unlocalizedName));
	}
	
	/** Removes this copy block data if assigned to any player*/
	public void handleBlockDestroyed(World world, BlockPos pos)
	{
		if(world.isRemote)
			return;
		
		for(EntityPlayer player : world.playerEntities)
		{
			NBTTagCompound tagEntityData = player.getEntityData();
			NBTTagList copyBlocks = tagEntityData.getList("CopyBlocks", 10);
			
			if(copyBlocks == null)
				copyBlocks = new NBTTagList();
			
			for (int i = 0; i < copyBlocks.toArray().length; i++)
			{
				BlockPos currentBlockPos = TagHelper.ReadBlockPos(copyBlocks.getCompound(i));
				if(Compare.BlockPosIsEqual(currentBlockPos, pos))
				{
					copyBlocks.removeTag(i);
					Chat.send(player, ">Copy block destroyed", TextFormatting.RED);
					break;
				}
			}
			
			tagEntityData.setTag("CopyBlocks", copyBlocks);
			player.writeUnlessRemoved(tagEntityData);
		}
	}
	
	
	/** Assigns this copy block to the placer entity and removes old copy blocks if needed*/
	public void handleBlockAdded(EntityLivingBase placer, World worldIn, BlockPos pos)
	{
		if(worldIn.isRemote)
			return;
		
		NBTTagCompound tagEntityData = placer.getEntityData();
		NBTTagList copyBlocks = tagEntityData.getList("CopyBlocks", 10);
		
		if(copyBlocks == null)
			copyBlocks = new NBTTagList();
		
		if(copyBlocks.toArray().length >= 2)
		{
			for (int i = copyBlocks.toArray().length-1; i >= 0; i--)
			{
				BlockPos currentBlockPos = TagHelper.ReadBlockPos(copyBlocks.getCompound(i));
				copyBlocks.removeTag(i);
				worldIn.destroyBlock(currentBlockPos, true);
			}
			Chat.send(placer, ">Destroyed old copy blocks", TextFormatting.RED);
		}
		
		copyBlocks.add(TagHelper.BlockPosToCompoundTag(pos));
		Chat.send(placer, "Added new copy block [" + copyBlocks.toArray().length + "/2]", TextFormatting.BLUE);
		
		tagEntityData.setTag("CopyBlocks", copyBlocks);
		placer.writeUnlessRemoved(tagEntityData);
	}
	
	
	/** If the activating player is holding the build tool, rotates the copy region */
	public void handleBlockActivated(EntityPlayer player, EnumHand hand, BlockPos pos)
	{
		if(player.getHeldItem(hand) == null)
			return;
		
		if(player.getHeldItem(hand).getItem() == null)
			return;
		
		if (player.getHeldItem(hand).getItem() == Items.buildTool)
		{	
			player.world.playSound(
					player,
					player.getPosition(),
					Sounds.BUILD_TOOL_ROTATE,
					SoundCategory.PLAYERS,
					0.5F,
					player.world.rand.nextFloat() * 0.1F + 0.9F);
			
			if(player.world.isRemote)
				return;
			
			NBTTagCompound tagEntityData = player.getEntityData();
			NBTTagList copyBlocks = tagEntityData.getList("CopyBlocks", 10);
			
			if(copyBlocks == null)
				copyBlocks = new NBTTagList();
			
			if(copyBlocks.toArray().length == 2)
			{
				Rotate.rotate(
						TagHelper.ReadBlockPos(copyBlocks.getCompound(0)),
						TagHelper.ReadBlockPos(copyBlocks.getCompound(1)).add(0,50,0),
						player,
						player.world);
			}
		}
	}
	
	@Override
	public void onPlayerDestroy(IWorld worldIn, BlockPos pos, IBlockState state)
    {
		handleBlockDestroyed(worldIn.getWorld(), pos);
    }
	
	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn)
    {
		handleBlockDestroyed(worldIn, pos);
    }
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
		handleBlockAdded(placer, worldIn, pos);
    }
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
		handleBlockDestroyed(worldIn, pos);
    }
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player,
			EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		handleBlockActivated(player, hand, pos);
		return false;
	}
}