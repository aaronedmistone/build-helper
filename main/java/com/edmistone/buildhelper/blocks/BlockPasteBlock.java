package com.edmistone.buildhelper.blocks;

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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/** Paste Block when placed and activated with a build tool will clone the copy region (if set)
 *  to the paste block location in the shown direction (paste blocks visually show the direction)
 *  @author Aaron Edmistone */
public class BlockPasteBlock extends Block
{
	protected static VoxelShape SHAPE = Block.makeCuboidShape(5.0D, 5.0D, 5.0D, 11.0D, 10.0D, 11.0D);
	
	public BlockPasteBlock(String unlocalizedName)
	{
		super(Properties.create(Material.GROUND).hardnessAndResistance(1, 20));
		this.setRegistryName(new ResourceLocation(Info.MODID, unlocalizedName));
	}
	
	@Override
    public boolean isNormalCube(IBlockState state)
	{
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
        Vec3d vec3d = state.getOffset(worldIn, pos);
        SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 15.0D, 7.0D, 15.0D);
        return SHAPE.withOffset(vec3d.x, vec3d.y, vec3d.z);
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
			NBTTagList pasteBlocks = tagEntityData.getList("PasteBlocks", 10);
			
			if(pasteBlocks == null)
				pasteBlocks = new NBTTagList();
			
			for (int i = 0; i < pasteBlocks.toArray().length; i++)
			{
				BlockPos currentBlockPos = TagHelper.ReadBlockPos(pasteBlocks.getCompound(i));
				if(Compare.BlockPosIsEqual(currentBlockPos, pos))
				{
					pasteBlocks.removeTag(i);
					player.sendMessage(new TextComponentString(TextFormatting.RED + ">Removed paste block"));
					break;
				}
			}
			
			tagEntityData.setTag("PasteBlocks", pasteBlocks);
			player.writeUnlessRemoved(tagEntityData);
		}
	}
	
	
	/** Assigns this paste block to the placer entity and removes old paste block if needed*/
	public void handleBlockAdded(EntityLivingBase placer, World worldIn, BlockPos pos)
	{
		if(worldIn.isRemote)
			return;
		
		NBTTagCompound tagEntityData = placer.getEntityData();
		NBTTagList pasteBlocks = tagEntityData.getList("PasteBlocks", 10);
		
		if(pasteBlocks == null)
			pasteBlocks = new NBTTagList();
		
		if(pasteBlocks.toArray().length >= 1)
		{
			for (int i = pasteBlocks.toArray().length-1; i >= 0; i--)
			{
				BlockPos currentBlockPos = TagHelper.ReadBlockPos(pasteBlocks.getCompound(i));
				pasteBlocks.removeTag(i);
				worldIn.destroyBlock(currentBlockPos, true);
			}
			
			placer.sendMessage(new TextComponentString(TextFormatting.RED + ">Destroyed old paste block"));
		}
		
		pasteBlocks.add(TagHelper.BlockPosToCompoundTag(pos));
		
		tagEntityData.setTag("PasteBlocks", pasteBlocks);
		placer.writeUnlessRemoved(tagEntityData);
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
			player.world.playSound(
					player,
					player.getPosition(),
					Sounds.BUILD_TOOL_USE,
					SoundCategory.PLAYERS,
					0.5F,
					player.world.rand.nextFloat() * 0.1F + 0.9F);
			
			if(player.world.isRemote)
				return;
			
			NBTTagCompound tagEntityData = player.getEntityData();
			NBTTagList copyBlocks = tagEntityData.getList("CopyBlocks", 10);
			NBTTagList pasteBlocks = tagEntityData.getList("PasteBlocks", 10);
			
			if(copyBlocks == null)
				copyBlocks = new NBTTagList();
			
			if(pasteBlocks == null)
				pasteBlocks = new NBTTagList();
			
			if(copyBlocks.toArray().length == 2 && pasteBlocks.toArray().length == 1)
			{
				BlockPos pasteBlockPos = TagHelper.ReadBlockPos(pasteBlocks.getCompound(0));
				pasteBlocks.removeTag(0);
				player.world.destroyBlock(pasteBlockPos, true);
				
				Clone.clone(
						TagHelper.ReadBlockPos(copyBlocks.getCompound(0)),
						TagHelper.ReadBlockPos(copyBlocks.getCompound(1)).add(0,50,0),
						pasteBlockPos,
						Clone.CloneMode.FORCE,
						player,
						player.world,
						useVariantClone());
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