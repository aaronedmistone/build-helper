package com.edmistone.buildhelper.events;

import com.edmistone.buildhelper.helpers.BlockHelper;
import com.edmistone.buildhelper.registry.Blocks;
import com.edmistone.buildhelper.registry.Items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class EventPlayerInteraction
{
	public static void onPlayerInteract(PlayerInteractEvent event)
	{
		if( event.getPlayer().isCrouching() && event.getItemStack() != ItemStack.EMPTY)
		{
			Item item = event.getItemStack().getItem();
			Item newItem = null;
			
			if(item == Items.symmetryTool)
			{
				newItem = Items.buildTool;
				event.setCanceled(true);
			}
			else if(item == Items.buildTool)
			{
				newItem = BlockHelper.BlockToItem(Blocks.copyBlock);
				event.setCanceled(true);
			}
			else if(item == BlockHelper.BlockToItem(Blocks.copyBlock))
			{
				newItem = BlockHelper.BlockToItem(Blocks.pasteBlock);
				event.setCanceled(true);
			}
			else if(item == BlockHelper.BlockToItem(Blocks.pasteBlock))
			{
				newItem = Items.buildTool;
				if(event.getPlayer().hasPermissionLevel(3))
					newItem = BlockHelper.BlockToItem(Blocks.pasteVariantBlock);
				
				event.setCanceled(true);
			}
			else if(item == BlockHelper.BlockToItem(Blocks.pasteVariantBlock))
			{
				newItem = Items.buildTool;
				if(event.getPlayer().hasPermissionLevel(3))
					newItem = Items.symmetryTool;
				
				event.setCanceled(true);
			}
			
			if(newItem != null)
			{
				event.getPlayer().setHeldItem(event.getHand(), newItem.getDefaultInstance());
			}
		}
	}
}
