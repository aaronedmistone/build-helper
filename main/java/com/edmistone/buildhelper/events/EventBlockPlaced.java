package com.edmistone.buildhelper.events;

import com.edmistone.buildhelper.process.ProcessSymmetry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class EventBlockPlaced
{
	/** Fires when any block is placed in the world **/
	public static void onBlockPlaced(BlockEvent.PlaceEvent event)
	{
		EntityPlayer player = event.getPlayer();
		World world = event.getWorld();
	    NBTTagCompound playerTags = player.getEntityData();
	    BlockPos blockPos = event.getPos();
	    IBlockState block = event.getPlacedBlock();
	    
	    ProcessSymmetry.processSymmetry(world, blockPos, playerTags, block);
	}
}
