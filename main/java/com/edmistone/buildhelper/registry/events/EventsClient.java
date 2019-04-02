package com.edmistone.buildhelper.registry.events;

import com.edmistone.buildhelper.events.EventRenderWorldLast;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Client only subscribed events */
public class EventsClient
{
	public static EventsClient instance = new EventsClient();
	
	@SubscribeEvent
	public void eventrenderWorldLast(RenderWorldLastEvent event)
	{
		EventRenderWorldLast.onRenderWorldLast(event);
	}
}
