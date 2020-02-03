package com.edmistone.buildhelper.events;

import com.edmistone.buildhelper.blocks.BlockCopyBlock;
import com.edmistone.buildhelper.blocks.BlockPasteBlock;
import com.edmistone.buildhelper.process.ProcessSymmetry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

/** @author Aaron Edmistone */
public class EventEntityJoining
{
	/** Fires when any entity joins the world **/
	public static void onEntityJoining(EntityJoinWorldEvent event)
	{
		if(!(event.getEntity() instanceof PlayerEntity))
			return;
		
		PlayerEntity player = (PlayerEntity) event.getEntity();
		CompoundNBT playerData = player.getPersistentData();
		
		ProcessSymmetry.clearSymmetryData(player, playerData);
		BlockCopyBlock.clearCopyBlockData(player, playerData);
		BlockPasteBlock.clearPasteBlockData(player, playerData);
	}
}
