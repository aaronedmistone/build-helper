package com.edmistone.buildhelper.registry.events;

import com.edmistone.buildhelper.events.EventBlockDestroyed;
import com.edmistone.buildhelper.events.EventBlockPlaced;
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
	
	//TODO: BlockEvent.PlaceEvent is not firing, is this a forge 1.13.2 bug?
	@SubscribeEvent
	public void eventBlockPlaced(BlockEvent.PlaceEvent event)
	{
	   EventBlockPlaced.onBlockPlaced(event);
	}
}
