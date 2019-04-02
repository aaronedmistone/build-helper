package com.edmistone.buildhelper.registry.events;

import com.edmistone.buildhelper.events.EventBlockDestroyed;
import com.edmistone.buildhelper.events.EventBlockPlaced;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**  Shared subscribed events (client and server)*/
public class EventsCommon
{
	public static EventsCommon instance = new EventsCommon();
	
	@SubscribeEvent
	public void eventBlockDestroyed(BlockEvent.BreakEvent event)
	{
		EventBlockDestroyed.onBlockDestroyed(event);
	}
	
	@SubscribeEvent
	public void eventBlockPlaced(BlockEvent.PlaceEvent event)
	{
	   EventBlockPlaced.onBlockPlaced(event);
	}
}
