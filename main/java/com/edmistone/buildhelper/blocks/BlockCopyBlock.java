package com.edmistone.buildhelper.blocks;

import javax.annotation.Nullable;

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
import net.minecraft.world.World;

/** Copy Block can be placed on 2 sides (in a diagonal line) of a section to be copied/rotated.
 *  Sections can be pasted using a paste block or rotated by using build tool on the copy block.*/
public class BlockCopyBlock extends Block
{
	public BlockCopyBlock(String unlocalizedName)
	{
		super(Material.GROUND);
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName(new ResourceLocation(Info.MODID, unlocalizedName));
		this.setHardness(1);
		this.setResistance(20);
	}
	
	/** Removes this copy block data if assigned to any player*/
	public void handleBlockDestroyed(World world, BlockPos pos)
	{
		if(world.isRemote)
			return;
		
		for(EntityPlayer player : world.playerEntities)
		{
			NBTTagCompound tagEntityData = player.getEntityData();
			NBTTagList copyBlocks = tagEntityData.getTagList("CopyBlocks", 10);
			
			if(copyBlocks == null)
				copyBlocks = new NBTTagList();
			
			for (int i = 0; i < copyBlocks.tagCount(); i++)
			{
				BlockPos currentBlockPos = TagHelper.ReadBlockPos(copyBlocks.getCompoundTagAt(i));
				if(Compare.BlockPosIsEqual(currentBlockPos, pos))
				{
					copyBlocks.removeTag(i);
					Chat.send(player, ">Copy block destroyed", TextFormatting.RED);
					break;
				}
			}
			
			tagEntityData.setTag("CopyBlocks", copyBlocks);
			player.writeToNBTAtomically(tagEntityData);
		}
	}
	
	
	/** Assigns this copy block to the placer entity and removes old copy blocks if needed*/
	public void handleBlockAdded(EntityLivingBase placer, World worldIn, BlockPos pos)
	{
		if(worldIn.isRemote)
			return;
		
		NBTTagCompound tagEntityData = placer.getEntityData();
		NBTTagList copyBlocks = tagEntityData.getTagList("CopyBlocks", 10);
		
		if(copyBlocks == null)
			copyBlocks = new NBTTagList();
		
		if(copyBlocks.tagCount() >= 2)
		{
			for (int i = copyBlocks.tagCount()-1; i >= 0; i--)
			{
				BlockPos currentBlockPos = TagHelper.ReadBlockPos(copyBlocks.getCompoundTagAt(i));
				copyBlocks.removeTag(i);
				worldIn.destroyBlock(currentBlockPos, true);
			}
			Chat.send(placer, ">Destroyed old copy blocks", TextFormatting.RED);
		}
		
		copyBlocks.appendTag(TagHelper.BlockPosToCompoundTag(pos));
		Chat.send(placer, "Added new copy block [" + copyBlocks.tagCount() + "/2]", TextFormatting.BLUE);
		
		tagEntityData.setTag("CopyBlocks", copyBlocks);
		placer.writeToNBTAtomically(tagEntityData);
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
			player.worldObj.playSound(
					player,
					player.getPosition(),
					Sounds.BUILD_TOOL_ROTATE,
					SoundCategory.PLAYERS,
					0.5F,
					player.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			
			if(player.worldObj.isRemote)
				return;
			
			NBTTagCompound tagEntityData = player.getEntityData();
			NBTTagList copyBlocks = tagEntityData.getTagList("CopyBlocks", 10);
			
			if(copyBlocks == null)
				copyBlocks = new NBTTagList();
			
			if(copyBlocks.tagCount() == 2)
			{
				Rotate.rotate(
						TagHelper.ReadBlockPos(copyBlocks.getCompoundTagAt(0)),
						TagHelper.ReadBlockPos(copyBlocks.getCompoundTagAt(1)).add(0,50,0),
						player,
						player.worldObj);
			}
		}
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
		handleBlockDestroyed(worldIn, pos);
    }
	
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		handleBlockActivated(player, hand, pos);
		return false;
	}
}