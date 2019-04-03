package com.edmistone.buildhelper.helpers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/** Helper functions for Tag classes 
 * @author Aaron Edmistone */
public class TagHelper
{
	public static BlockPos ReadBlockPos(NBTTagCompound compoundTag)
	{
		int posX = compoundTag.getInt("posX");
		int posY = compoundTag.getInt("posY");
		int posZ = compoundTag.getInt("posZ");
		return new BlockPos(posX, posY, posZ);
	}
	
	public static NBTTagCompound BlockPosToCompoundTag(BlockPos pos)
	{
		NBTTagCompound compoundTag = new NBTTagCompound();
		compoundTag.setInt("posX", pos.getX());
		compoundTag.setInt("posY", pos.getY());
		compoundTag.setInt("posZ", pos.getZ());
		return compoundTag;
	}
}
