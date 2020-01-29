package com.edmistone.buildhelper.registry.events;

import com.edmistone.buildhelper.events.EventEntityJoining;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/** Server only subscribed events 
 *  @author Aaron Edmistone */
public class EventsServer
{
	public static EventsServer instance = new EventsServer();
	
	@SubscribeEvent
	public void eventEntityJoining(EntityJoinWorldEvent event)
	{
		EventEntityJoining.onEntityJoining(event);
	}
}
