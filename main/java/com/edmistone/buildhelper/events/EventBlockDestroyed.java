package com.edmistone.buildhelper.events;

import com.edmistone.buildhelper.process.ProcessSymmetry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class EventBlockDestroyed
{
	/** Fired when any block breaks in the world */
	public static void onBlockDestroyed(BlockEvent.BreakEvent event)
	{
		EntityPlayer player = event.getPlayer();
		World world = event.getWorld();
	    NBTTagCompound playerTags = player.getEntityData();
	    BlockPos blockPos = event.getPos();
	    
	    ProcessSymmetry.processSymmetry(world, blockPos, playerTags, null);
	}
}
