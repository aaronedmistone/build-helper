package com.edmistone.buildhelper.helpers;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Helper functions for Block class 
 *  @author Aaron Edmistone */
public class BlockHelper
{
	public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
	
	/** Returns an IBlockState with the facing property flipped on the given axis */
	public static IBlockState FlipFacing(IBlockState blockState, boolean northSouth, boolean eastWest)
	{
		if(blockState.getProperties().contains(FACING))
		{
			if(northSouth && blockState.get(FACING) == EnumFacing.NORTH)
			{
				return blockState.with(FACING, EnumFacing.SOUTH);
			} 
			else if (eastWest && blockState.get(FACING) == EnumFacing.EAST)
			{
				return blockState.with(FACING, EnumFacing.WEST);
			}
			else if (northSouth && blockState.get(FACING) == EnumFacing.SOUTH)
			{
				return blockState.with(FACING, EnumFacing.NORTH);
			}
			else if (eastWest && blockState.get(FACING) == EnumFacing.WEST)
			{
				return blockState.with(FACING, EnumFacing.EAST);
			}
		}
		
		return blockState;
	}
	
	/** Returns an IBlockState with the facing property rotated CW 90 degrees */
	public static IBlockState RotateFacing(IBlockState blockState)
	{
		if(blockState.getProperties().contains(FACING))
		{
			if(blockState.get(FACING) == EnumFacing.NORTH)
			{
				return blockState.with(FACING, EnumFacing.EAST);
			} 
			else if (blockState.get(FACING) == EnumFacing.EAST)
			{
				return blockState.with(FACING, EnumFacing.SOUTH);
			}
			else if (blockState.get(FACING) == EnumFacing.SOUTH)
			{
				return blockState.with(FACING, EnumFacing.WEST);
			}
			else if (blockState.get(FACING) == EnumFacing.WEST)
			{
				return blockState.with(FACING, EnumFacing.NORTH);
			}
		}
		
		cycleBlockVariant(null, null, 0, 0);
		
		return blockState;
	}
	
	/** Broken in 1.13.2. 
	 * {@link com.edmistone.buildhelper.blocks.BlockPasteVariantBlock} disabled until fixed.
	 * Cycles the block 'variant' and 'color' properties of a block by the given amounts*/
	@Deprecated
	public static boolean cycleBlockVariant(World world, BlockPos pos, int variantCycles, int colorCycles)
    {
		boolean result = false;
        IBlockState state = world.getBlockState(pos);
        for (IProperty<?> prop : state.getProperties())
        {
        	if (prop.getName().equals("variant"))
            {
            	for(int i = 0; i < variantCycles; i++)
            		state = state.cycle(prop);
        		
            	world.setBlockState(pos, state);
            	result = true;
            }
        	else if (prop.getName().equals("color"))
            {
            	for(int i = 0; i < colorCycles; i++)
            		state = state.cycle(prop);
            		
            	world.setBlockState(pos, state);
            	result = true;
            }
        }
        return result;
    }
	
	/** Deletes a block */
	public static void deleteBlock(World world, BlockPos pos)
	{
		TileEntity oldSourceTileEntity = world.getTileEntity(pos);

        if (oldSourceTileEntity instanceof IInventory)
            ((IInventory)oldSourceTileEntity).clear();
        
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
	}
}
