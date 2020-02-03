package com.edmistone.buildhelper.registry.events;

import com.edmistone.buildhelper.events.EventPlayerInteraction;
import com.edmistone.buildhelper.events.EventRenderWorldLast;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/** Client only subscribed events 
 *  @author Aaron Edmistone */
public class EventsClient
{
	public static EventsClient instance = new EventsClient();
	
	@SubscribeEvent
	public void eventrenderWorldLast(RenderWorldLastEvent event)
	{
		EventRenderWorldLast.onRenderWorldLast(event);
	}
	
	@SubscribeEvent
	public void eventPlayerInteractEvent(PlayerInteractEvent event)
	{
	    EventPlayerInteraction.onPlayerInteract(event);
	}
}
