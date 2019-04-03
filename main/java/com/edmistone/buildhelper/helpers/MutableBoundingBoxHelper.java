package com.edmistone.buildhelper.helpers;

import net.minecraft.util.math.MutableBoundingBox;

/** Helper functions for MutableBoundingBox class 
 * @author Aaron Edmistone */
public class MutableBoundingBoxHelper
{
	/** Reduces the bounding box by 1 block on each side (e.g. 4x4x4 = 2x2x2) */
	public static MutableBoundingBox reduceBoundingBox(MutableBoundingBox box)
	{
		return new MutableBoundingBox( box.minX + 1,
										 box.minY + 1,
										 box.minZ + 1,
										 box.maxX - 1,
										 box.maxY - 1,
										 box.maxZ - 1);
	}
}
