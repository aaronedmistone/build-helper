package com.edmistone.buildhelper.operations;

import net.minecraft.util.math.BlockPos;

/** Functions for calculation of various things */
public class Calculate
{
	/** Calculate the distance between two block positions */
	public static double distance(BlockPos pos1, BlockPos pos2)
	{
		double deltaX = pos1.getX() - pos2.getX();
		double deltaY = pos1.getY() - pos2.getY();
		double deltaZ = pos1.getZ() - pos2.getZ();
			
		return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
	}
}
