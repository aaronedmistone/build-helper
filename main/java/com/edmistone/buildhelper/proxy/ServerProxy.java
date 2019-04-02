package com.edmistone.buildhelper.proxy;

import com.edmistone.buildhelper.registry.events.EventsServer;

import net.minecraftforge.common.MinecraftForge;

/** Server proxy for registering server-only things */
public class ServerProxy extends CommonProxy
{

	@Override
	public void preInit()
	{
		super.preInit();
	}

	@Override
	public void init()
	{
		super.init();
		
		//register events for the server only
		MinecraftForge.EVENT_BUS.register(EventsServer.instance);
	}
}
