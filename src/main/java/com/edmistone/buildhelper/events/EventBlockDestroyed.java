package com.edmistone.buildhelper.events;

import com.edmistone.buildhelper.process.ProcessSymmetry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

/** @author Aaron Edmistone */
public class EventBlockDestroyed
{
	/** Fired when any block breaks in the world */
	public static void onBlockDestroyed(BlockEvent.BreakEvent event)
	{
		PlayerEntity player = event.getPlayer();
		World world = event.getWorld().getWorld();
		CompoundNBT playerTags = player.getPersistentData();
	    BlockPos blockPos = event.getPos();
	    
	    ProcessSymmetry.processSymmetry(world, blockPos, playerTags, null);
	}
}
