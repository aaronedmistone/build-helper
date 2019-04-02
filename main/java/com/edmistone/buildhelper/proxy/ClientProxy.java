package com.edmistone.buildhelper.proxy;

import com.edmistone.buildhelper.registry.Blocks;
import com.edmistone.buildhelper.registry.Items;
import com.edmistone.buildhelper.registry.events.EventsClient;

import net.minecraftforge.common.MinecraftForge;

/** Client proxy for registering client-only things */
public class ClientProxy extends CommonProxy
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
		
		//register events for the client only
		MinecraftForge.EVENT_BUS.register(EventsClient.instance);
	}

	@Override
	public void registerRenders()
	{
		Items.registerRenders();
		Blocks.registerRenders();
	}

}