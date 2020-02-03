package com.edmistone.buildhelper.registry.events;

import com.edmistone.buildhelper.events.EventBlockDestroyed;
import com.edmistone.buildhelper.events.EventBlockPlaced;
import com.edmistone.buildhelper.events.EventEntityJoining;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**  Shared subscribed events (client and server)
 *  @author Aaron Edmistone */
public class EventsCommon
{
	public static EventsCommon instance = new EventsCommon();
	
	@SubscribeEvent
	public void eventBlockDestroyed(BlockEvent.BreakEvent event)
	{
		EventBlockDestroyed.onBlockDestroyed(event);
	}
	
	@SubscribeEvent
	public void eventBlockPlaced(BlockEvent.EntityPlaceEvent event)
	{
	   EventBlockPlaced.onBlockPlaced(event);
	}
	
	@SubscribeEvent
	public void eventEntityJoining(EntityJoinWorldEvent event)
	{
		EventEntityJoining.onEntityJoining(event);
	}
}
