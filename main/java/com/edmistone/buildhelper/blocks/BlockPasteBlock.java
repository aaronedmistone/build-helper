package com.edmistone.buildhelper.blocks;

import javax.annotation.Nullable;

import com.edmistone.buildhelper.Info;
import com.edmistone.buildhelper.helpers.TagHelper;
import com.edmistone.buildhelper.operations.Clone;
import com.edmistone.buildhelper.operations.Compare;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/** Paste Block when placed and activated with a build tool will clone the copy region (if set)
 *  to the paste block location in the shown direction (paste blocks visually show the direction)*/
public class BlockPasteBlock extends Block
{
	public BlockPasteBlock(String unlocalizedName)
	{
		super(Material.GROUND);
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName(new ResourceLocation(Info.MODID, unlocalizedName));
		this.setHardness(1);
		this.setResistance(20);
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state)
	{
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    public boolean useVariantClone()
    {
    	return false;
    }
	
    /** Removes this paste block data if assigned to any player*/
	public void handleBlockDestroyed(World world, BlockPos pos)
	{
		if(world.isRemote)
			return;
		
		for(EntityPlayer player : world.playerEntities)
		{
			NBTTagCompound tagEntityData = player.getEntityData();
			NBTTagList pasteBlocks = tagEntityData.getTagList("PasteBlocks", 10);
			
			if(pasteBlocks == null)
				pasteBlocks = new NBTTagList();
			
			for (int i = 0; i < pasteBlocks.tagCount(); i++)
			{
				BlockPos currentBlockPos = TagHelper.ReadBlockPos(pasteBlocks.getCompoundTagAt(i));
				if(Compare.BlockPosIsEqual(currentBlockPos, pos))
				{
					pasteBlocks.removeTag(i);
					player.addChatMessage(new TextComponentString(TextFormatting.RED + ">Removed paste block"));
					break;
				}
			}
			
			tagEntityData.setTag("PasteBlocks", pasteBlocks);
			player.writeToNBTAtomically(tagEntityData);
		}
	}
	
	
	/** Assigns this paste block to the placer entity and removes old paste block if needed*/
	public void handleBlockAdded(EntityLivingBase placer, World worldIn, BlockPos pos)
	{
		if(worldIn.isRemote)
			return;
		
		NBTTagCompound tagEntityData = placer.getEntityData();
		NBTTagList pasteBlocks = tagEntityData.getTagList("PasteBlocks", 10);
		
		if(pasteBlocks == null)
			pasteBlocks = new NBTTagList();
		
		if(pasteBlocks.tagCount() >= 1)
		{
			for (int i = pasteBlocks.tagCount()-1; i >= 0; i--)
			{
				BlockPos currentBlockPos = TagHelper.ReadBlockPos(pasteBlocks.getCompoundTagAt(i));
				pasteBlocks.removeTag(i);
				worldIn.destroyBlock(currentBlockPos, true);
			}
			
			placer.addChatMessage(new TextComponentString(TextFormatting.RED + ">Destroyed old paste block"));
		}
		
		pasteBlocks.appendTag(TagHelper.BlockPosToCompoundTag(pos));
		
		tagEntityData.setTag("PasteBlocks", pasteBlocks);
		placer.writeToNBTAtomically(tagEntityData);
	}
	
	/** If the activating player is holding the build tool, pastes the copy region */
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
					Sounds.BUILD_TOOL_USE,
					SoundCategory.PLAYERS,
					0.5F,
					player.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			
			if(player.worldObj.isRemote)
				return;
			
			NBTTagCompound tagEntityData = player.getEntityData();
			NBTTagList copyBlocks = tagEntityData.getTagList("CopyBlocks", 10);
			NBTTagList pasteBlocks = tagEntityData.getTagList("PasteBlocks", 10);
			
			if(copyBlocks == null)
				copyBlocks = new NBTTagList();
			
			if(pasteBlocks == null)
				pasteBlocks = new NBTTagList();
			
			if(copyBlocks.tagCount() == 2 && pasteBlocks.tagCount() == 1)
			{
				BlockPos pasteBlockPos = TagHelper.ReadBlockPos(pasteBlocks.getCompoundTagAt(0));
				pasteBlocks.removeTag(0);
				player.worldObj.destroyBlock(pasteBlockPos, true);
				
				Clone.clone(
						TagHelper.ReadBlockPos(copyBlocks.getCompoundTagAt(0)),
						TagHelper.ReadBlockPos(copyBlocks.getCompoundTagAt(1)).add(0,50,0),
						pasteBlockPos,
						Clone.CloneMode.FORCE,
						player,
						player.worldObj,
						useVariantClone());
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