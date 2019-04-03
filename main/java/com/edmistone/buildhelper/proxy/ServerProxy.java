package com.edmistone.buildhelper.proxy;

import com.edmistone.buildhelper.registry.events.EventsServer;

import net.minecraftforge.common.MinecraftForge;

/** Server proxy for registering server-only things 
 *  @author Aaron Edmistone */
public class ServerProxy extends CommonProxy
{
	@Override
	public void hookEvents()
	{
		super.hookEvents();
		
		//register events for the server only
		MinecraftForge.EVENT_BUS.register(EventsServer.instance);
	}
}
