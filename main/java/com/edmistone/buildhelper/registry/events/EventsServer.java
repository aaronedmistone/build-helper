package com.edmistone.buildhelper.registry.events;

import com.edmistone.buildhelper.events.EventEntityJoining;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Server only subscribed events */
public class EventsServer
{
	public static EventsServer instance = new EventsServer();
	
	@SubscribeEvent
	public void eventEntityJoining(EntityJoinWorldEvent event)
	{
		EventEntityJoining.onEntityJoining(event);
	}
}
