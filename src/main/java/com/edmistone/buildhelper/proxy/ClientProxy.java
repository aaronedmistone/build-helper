package com.edmistone.buildhelper.proxy;

import com.edmistone.buildhelper.registry.events.EventsClient;

import net.minecraftforge.common.MinecraftForge;

/** Client proxy for registering client-only things 
 *  @author Aaron Edmistone */
public class ClientProxy extends CommonProxy
{
	@Override
	public void hookEvents()
	{
		super.hookEvents();
		
		//register events for the client only
		MinecraftForge.EVENT_BUS.register(EventsClient.instance);
	}
}