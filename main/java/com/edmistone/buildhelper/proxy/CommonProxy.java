package com.edmistone.buildhelper.proxy;

import com.edmistone.buildhelper.registry.events.EventsCommon;

import net.minecraftforge.common.MinecraftForge;

/** Common proxy methods should remain empty.
 *  Client things in ClientProxy.
 *  Server things in ServerProxy.
 *  Common things in main mod class.
 * @author Aaron Edmistone
 */
public class CommonProxy
{
	public void hookEvents()
	{
		//register events for both the client and server (common events)
		MinecraftForge.EVENT_BUS.register(EventsCommon.instance);
	}
}