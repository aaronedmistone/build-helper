package com.edmistone.buildhelper.helpers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

/** Helper functions for Tag classes 
 * @author Aaron Edmistone */
public class TagHelper
{
	public static BlockPos ReadBlockPos(CompoundNBT compoundTag)
	{
		int posX = compoundTag.getInt("posX");
		int posY = compoundTag.getInt("posY");
		int posZ = compoundTag.getInt("posZ");
		return new BlockPos(posX, posY, posZ);
	}
	
	public static CompoundNBT BlockPosToCompoundTag(BlockPos pos)
	{
		CompoundNBT compoundTag = new CompoundNBT();
		compoundTag.putInt("posX", pos.getX());
		compoundTag.putInt("posY", pos.getY());
		compoundTag.putInt("posZ", pos.getZ());
		return compoundTag;
	}
}
