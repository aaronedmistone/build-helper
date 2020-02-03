package com.edmistone.buildhelper.helpers;

import javax.annotation.Nullable;

import com.edmistone.buildhelper.operations.BlockVariant;

import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Helper functions for Block class 
 *  @author Aaron Edmistone */
public class BlockHelper
{
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final EnumProperty<StairsShape> STAIRS_SHAPE = BlockStateProperties.STAIRS_SHAPE;
	
	/** Returns an IBlockState with the facing property flipped on the given axis */
	public static BlockState FlipFacing(BlockState blockState, boolean northSouth, boolean eastWest)
	{		
		if(blockState.getProperties().contains(STAIRS_SHAPE) && (!northSouth || !eastWest))
		{
			if(blockState.get(STAIRS_SHAPE) == StairsShape.INNER_LEFT)
			{
				blockState = blockState.with(STAIRS_SHAPE, StairsShape.INNER_RIGHT);
			}
			else if(blockState.get(STAIRS_SHAPE) == StairsShape.OUTER_LEFT)
			{
				blockState = blockState.with(STAIRS_SHAPE, StairsShape.OUTER_RIGHT);
			}
			else if(blockState.get(STAIRS_SHAPE) == StairsShape.INNER_RIGHT)
			{
				blockState = blockState.with(STAIRS_SHAPE, StairsShape.INNER_LEFT);
			}
			else if(blockState.get(STAIRS_SHAPE) == StairsShape.OUTER_RIGHT)
			{
				blockState = blockState.with(STAIRS_SHAPE, StairsShape.OUTER_LEFT);
			}
		}
		
		if(blockState.getProperties().contains(FACING))
		{
			if(northSouth && blockState.get(FACING) == Direction.NORTH)
			{
				blockState = blockState.with(FACING, Direction.SOUTH);
			} 
			else if (eastWest && blockState.get(FACING) == Direction.EAST)
			{
				blockState = blockState.with(FACING, Direction.WEST);
			}
			else if (northSouth && blockState.get(FACING) == Direction.SOUTH)
			{
				blockState = blockState.with(FACING, Direction.NORTH);
			}
			else if (eastWest && blockState.get(FACING) == Direction.WEST)
			{
				blockState = blockState.with(FACING, Direction.EAST);
			}
		}
		
		return blockState;
	}
	
	/** Returns an IBlockState with the facing property rotated CW 90 degrees */
	public static BlockState RotateFacing(BlockState blockState)
	{
		if(blockState.getProperties().contains(FACING))
		{
			if(blockState.get(FACING) == Direction.NORTH)
			{
				return blockState.with(FACING, Direction.EAST);
			} 
			else if (blockState.get(FACING) == Direction.EAST)
			{
				return blockState.with(FACING, Direction.SOUTH);
			}
			else if (blockState.get(FACING) == Direction.SOUTH)
			{
				return blockState.with(FACING, Direction.WEST);
			}
			else if (blockState.get(FACING) == Direction.WEST)
			{
				return blockState.with(FACING, Direction.NORTH);
			}
		}
		
		return blockState;
	}
	
	@Nullable
	public static Item BlockToItem(Block block)
	{
		return net.minecraftforge.registries.GameData.getBlockItemMap().get(block);
	}
	
	public static void DestroyBlockSilently(BlockPos pos, LivingEntity entity, Boolean spawnDrops)
	{
		BlockState state = entity.world.getBlockState(pos);
		Block.spawnDrops(state, entity.world, pos, null, entity, entity.getHeldItemMainhand());
		entity.world.setBlockState(pos, Blocks.AIR.getDefaultState());
	}
	
	/** Replaces a block by cycling the block variant by the given amounts and retaining the facing direction*/
	public static boolean setBlockStateWithVariantCycle(World world, BlockPos pos, BlockState currentBlockState, int variantCycles)
    {
		//BlockState currentBlockState = world.getBlockState(pos);
		Block currentBlock = currentBlockState.getBlock();
		BlockState newBlockState = BlockVariant.getVariant(currentBlock, variantCycles).getDefaultState();
		
		try
		{
			newBlockState = BlockStateHelper.copyProperties(currentBlockState, newBlockState);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
//		if(newBlockState.has(BlockStateProperties.BED_PART))
//		{ 
//			if(newBlockState.get(BlockStateProperties.BED_PART) == BedPart.FOOT)
//				return true;
//		}
		
		return world.setBlockState(pos, newBlockState, 2);
    }
	
	/** Deletes a block */
	public static void deleteBlock(World world, BlockPos pos)
	{
		TileEntity oldSourceTileEntity = world.getTileEntity(pos);

        if (oldSourceTileEntity instanceof IInventory)
            ((IInventory)oldSourceTileEntity).clear();
        
        world.setBlockState(pos, net.minecraft.block.Blocks.AIR.getDefaultState(), 3);
	}
}
