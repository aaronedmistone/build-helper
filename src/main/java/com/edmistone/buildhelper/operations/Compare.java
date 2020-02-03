package com.edmistone.buildhelper.operations;

import net.minecraft.util.math.BlockPos;

/** Contains comparison functions 
 *  @author Aaron Edmistone */
public class Compare
{
	/** Checks if the XYZ of the given BlockPos objects are equal */
	public static boolean BlockPosIsEqual(BlockPos pos1, BlockPos pos2)
	{
		if(pos1.getX() != pos2.getX())
			return false;
		
		if(pos1.getY() != pos2.getY())
			return false;
		
		if(pos1.getZ() != pos2.getZ())
			return false;
		
		return true;
	}
}
