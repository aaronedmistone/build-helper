package com.edmistone.buildhelper.events;

import com.edmistone.buildhelper.process.ProcessSymmetry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EventEntityJoining
{
	/** Fires when any entity joins the world **/
	public static void onEntityJoining(EntityJoinWorldEvent event)
	{
		if(!(event.getEntity() instanceof EntityPlayer))
			return;
		
		EntityPlayer player = (EntityPlayer) event.getEntity();
		NBTTagCompound playerData = player.getEntityData();
		
		ProcessSymmetry.clearSymmetryData(player, playerData);
	}
}
