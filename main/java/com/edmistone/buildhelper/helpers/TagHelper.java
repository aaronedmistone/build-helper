package com.edmistone.buildhelper.helpers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/** Helper functions for Tag classes */
public class TagHelper
{
	public static BlockPos ReadBlockPos(NBTTagCompound compoundTag)
	{
		int posX = compoundTag.getInteger("posX");
		int posY = compoundTag.getInteger("posY");
		int posZ = compoundTag.getInteger("posZ");
		return new BlockPos(posX, posY, posZ);
	}
	
	public static NBTTagCompound BlockPosToCompoundTag(BlockPos pos)
	{
		NBTTagCompound compoundTag = new NBTTagCompound();
		compoundTag.setInteger("posX", pos.getX());
		compoundTag.setInteger("posY", pos.getY());
		compoundTag.setInteger("posZ", pos.getZ());
		return compoundTag;
	}
}
