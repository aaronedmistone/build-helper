package com.edmistone.buildhelper.events;

import com.edmistone.buildhelper.process.ProcessSymmetry;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

/** @author Aaron Edmistone */
public class EventBlockPlaced
{
	/** Fires when any block is placed in the world **/
	public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event)
	{
		Entity player = event.getEntity();
		World world = event.getWorld().getWorld();
	    CompoundNBT playerTags = player.getPersistentData();
	    BlockPos blockPos = event.getPos();
	    BlockState block = event.getPlacedBlock();
	    
	    ProcessSymmetry.processSymmetry(world, blockPos, playerTags, block);
	}
}
