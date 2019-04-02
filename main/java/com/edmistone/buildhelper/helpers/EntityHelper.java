package com.edmistone.buildhelper.helpers;

import javax.vecmath.Vector3f;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/** Helper functions for Entity class */
public class EntityHelper
{
	public enum Direction
	{
		PositiveZ,
		NegativeX,
		NegativeZ,
		PositiveX
	}
	
	/** Returns the direction as an integer (0123, NESW, +Z-X-Z+Z, etc.) */
	public static int getDirectionInteger(Entity entity)
	{
		return MathHelper.floor_double((double)((entity.rotationYaw * 4F) / 360F) + 0.5D) & 3;
	}
	
	/** Returns the direction of the given entity */
	public static Direction getDirection(Entity entity)
	{
		return Direction.values()[getDirectionInteger(entity)];
	}
	
	/** Returns the name of the direction of the given entity */
	public static String getDirectionName(Entity entity)
	{
		return getDirection(entity).name();
	}
	
	/** Returns the true position of an entity corrected with partial ticks */
	public static Vector3f getTruePosition(Entity entity, float partialTicks)
	{
		Vector3f result = new Vector3f();
		result.x = (float) (entity.prevPosX + ( ( entity.posX - entity.prevPosX ) * partialTicks ));
		result.y = (float) (entity.prevPosY + ( ( entity.posY - entity.prevPosY ) * partialTicks ));
		result.z = (float) (entity.prevPosZ + ( ( entity.posZ - entity.prevPosZ ) * partialTicks ));
		
		return result;
	}
}
