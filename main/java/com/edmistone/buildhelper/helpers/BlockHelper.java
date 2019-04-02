package com.edmistone.buildhelper.helpers;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Helper functions for Block class */
public class BlockHelper
{	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	/** Returns an IBlockState with the facing property flipped on the given axis */
	public static IBlockState FlipFacing(IBlockState blockState, boolean northSouth, boolean eastWest)
	{
		if(blockState.getProperties().containsKey(FACING))
		{
			if(northSouth && blockState.getValue(FACING) == EnumFacing.NORTH)
			{
				return blockState.withProperty(FACING, EnumFacing.SOUTH);
			} 
			else if (eastWest && blockState.getValue(FACING) == EnumFacing.EAST)
			{
				return blockState.withProperty(FACING, EnumFacing.WEST);
			}
			else if (northSouth && blockState.getValue(FACING) == EnumFacing.SOUTH)
			{
				return blockState.withProperty(FACING, EnumFacing.NORTH);
			}
			else if (eastWest && blockState.getValue(FACING) == EnumFacing.WEST)
			{
				return blockState.withProperty(FACING, EnumFacing.EAST);
			}
		}
		
		return blockState;
	}
	
	/** Returns an IBlockState with the facing property rotated CW 90 degrees */
	public static IBlockState RotateFacing(IBlockState blockState)
	{
		if(blockState.getProperties().containsKey(FACING))
		{
			if(blockState.getValue(FACING) == EnumFacing.NORTH)
			{
				return blockState.withProperty(FACING, EnumFacing.EAST);
			} 
			else if (blockState.getValue(FACING) == EnumFacing.EAST)
			{
				return blockState.withProperty(FACING, EnumFacing.SOUTH);
			}
			else if (blockState.getValue(FACING) == EnumFacing.SOUTH)
			{
				return blockState.withProperty(FACING, EnumFacing.WEST);
			}
			else if (blockState.getValue(FACING) == EnumFacing.WEST)
			{
				return blockState.withProperty(FACING, EnumFacing.NORTH);
			}
		}
		
		return blockState;
	}
	
	
	@Deprecated
	/** This is a modified version of the source rotateBlock
	//The issue with this method is that the facing or rotation enums
	//also have up and down values and cycling the rotation makes no sense.
	//Leaving this here in case I am wrong or I am misunderstanding the purpose. **/
	public static boolean simpleRotateBlock(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        for (IProperty<?> prop : state.getProperties().keySet())
        {
            if (prop.getName().equals("facing") || prop.getName().equals("rotation"))
            {
                world.setBlockState(pos, state.cycleProperty(prop));
                return true;
            }
        }
        return false;
    }
	
	/** Cycles the block 'variant' and 'color' properties of a block by the given amounts*/
	public static boolean cycleBlockVariant(World world, BlockPos pos, int variantCycles, int colorCycles)
    {
		boolean result = false;
        IBlockState state = world.getBlockState(pos);
        for (IProperty<?> prop : state.getProperties().keySet())
        {
        	if (prop.getName().equals("variant"))
            {
            	for(int i = 0; i < variantCycles; i++)
            		state = state.cycleProperty(prop);
        		
            	world.setBlockState(pos, state);
            	result = true;
            }
        	else if (prop.getName().equals("color"))
            {
            	for(int i = 0; i < colorCycles; i++)
            		state = state.cycleProperty(prop);
            		
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
