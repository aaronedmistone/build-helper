package com.edmistone.buildhelper.helpers;

import net.minecraft.world.gen.structure.StructureBoundingBox;

/** Helper functions for StructureBoundingBox class */
public class StructureBoundingBoxHelper
{
	/** Reduces the bounding box by 1 block on each side (e.g. 4x4x4 = 2x2x2) */
	public static StructureBoundingBox reduceBoundingBox(StructureBoundingBox box)
	{
		return new StructureBoundingBox( box.minX + 1,
										 box.minY + 1,
										 box.minZ + 1,
										 box.maxX - 1,
										 box.maxY - 1,
										 box.maxZ - 1);
	}
}
