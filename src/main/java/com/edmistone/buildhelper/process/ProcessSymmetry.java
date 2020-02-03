package com.edmistone.buildhelper.process;

import javax.annotation.Nullable;

import com.edmistone.buildhelper.helpers.BlockHelper;
import com.edmistone.buildhelper.items.ItemSymmetryTool.SymmetryMode;
import com.edmistone.buildhelper.operations.ChestSearch;

import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Processing for symmetry system 
 *  @author Aaron Edmistone */
public class ProcessSymmetry
{
	/** Clear any player data related to the symmetry system */
	public static void clearSymmetryData(PlayerEntity player, CompoundNBT playerData)
	{
		playerData.putFloat("SymmetryPosX", 0);
		playerData.putFloat("SymmetryPosY", 0);
		playerData.putFloat("SymmetryPosZ", 0);
		playerData.putInt("SymmetryMode", 0);
		player.writeUnlessRemoved(playerData);
	}
	
	/** Process any blocks being placed or removed that are affected by the symmetry system */
	public static void processSymmetry(World world, BlockPos blockPos, CompoundNBT playerData, @Nullable BlockState block)
	{
		 if (world.isRemote)
		    	return;
		    
	    SymmetryMode symmetryMode = SymmetryMode.values()[playerData.getInt("SymmetryMode")];
	    
	    if(symmetryMode == SymmetryMode.None)
	    	return;
	    
		boolean symmetryNorthSouth =
				symmetryMode == SymmetryMode.NorthSouth || symmetryMode == SymmetryMode.Both;
		
		boolean symmetryEastWest =
				symmetryMode == SymmetryMode.EastWest || symmetryMode == SymmetryMode.Both;
	    
		float mx = playerData.getFloat("SymmetryPosX");
		float my = playerData.getFloat("SymmetryPosY");
		float mz = playerData.getFloat("SymmetryPosZ");
		
		if(!(blockPos.withinDistance(new BlockPos(mx,my,mz), 50)))
			return;
		
		BlockPos lastMirrorPos = new BlockPos(0,0,0);
		float lastDifference = 0;
		
		if(symmetryNorthSouth)
		{
			if(blockPos.getZ() > mz)
			{
				//paste block south
				float difference = blockPos.getZ() - mz;
				BlockPos pastePos = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() - difference * 2);
				lastMirrorPos = pastePos;
				
				if(pastePos != blockPos)
				{
					if(block != null)
					{
						world.setBlockState(pastePos, BlockHelper.FlipFacing(block, true, false));
					}
					else
					{
						world.setBlockState(pastePos, Blocks.AIR.getDefaultState(), 3);
					}
				}
			}
			else
			{
				//paste block north
				float difference = mz - blockPos.getZ();
				BlockPos pastePos = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() + difference * 2);
				lastMirrorPos = pastePos;
				
				if(pastePos != blockPos)
				{
					if(block != null)
					{
						world.setBlockState(pastePos, BlockHelper.FlipFacing(block, true, false));
					}
					else
					{
						world.setBlockState(pastePos, Blocks.AIR.getDefaultState(), 3);
					}
				}
			}
		}
		
		if(symmetryEastWest)
		{
			if(blockPos.getX() > mx)
			{
				//paste block west
				float difference = blockPos.getX() - mx;
				lastDifference = -difference;
				BlockPos pastePos = new BlockPos(blockPos.getX() - difference * 2, blockPos.getY(), blockPos.getZ());
				
				if(pastePos != blockPos)
				{
					if(block != null)
					{
						world.setBlockState(pastePos, BlockHelper.FlipFacing(block, false, true));
					}
					else
					{
						world.setBlockState(pastePos, Blocks.AIR.getDefaultState(), 3);
					}
				}
			}
			else
			{
				//paste block east
				float difference = mx - blockPos.getX();
				lastDifference = difference;
				BlockPos pastePos = new BlockPos(blockPos.getX() + difference * 2, blockPos.getY(), blockPos.getZ());
				
				if(pastePos != blockPos)
				{
					if(block != null)
					{
						world.setBlockState(pastePos, BlockHelper.FlipFacing(block, false, true));
					}
					else
					{
						world.setBlockState(pastePos, Blocks.AIR.getDefaultState(), 3);
					}
				}
			}
		}
		
		if(symmetryNorthSouth && symmetryEastWest)
		{
			BlockPos pastePos = new BlockPos(lastMirrorPos.getX() + lastDifference * 2, lastMirrorPos.getY(), lastMirrorPos.getZ());
			
			if(pastePos != blockPos)
			{
				if(block != null)
				{
					world.setBlockState(pastePos, BlockHelper.FlipFacing(block, true, true));
				}
				else
				{
					world.setBlockState(pastePos, Blocks.AIR.getDefaultState(), 3);
				}
			}
		}
	}
}
