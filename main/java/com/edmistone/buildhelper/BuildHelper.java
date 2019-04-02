package com.edmistone.buildhelper;

import com.edmistone.buildhelper.proxy.CommonProxy;
import com.edmistone.buildhelper.registry.Blocks;
import com.edmistone.buildhelper.registry.Items;
import com.edmistone.buildhelper.registry.Sounds;
import com.edmistone.buildhelper.registry.TileEntities;
import com.edmistone.buildhelper.registry.events.EventsCommon;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/** Main mod class, all shared/common registration should go here */
@Mod(modid = Info.MODID, version = Info.VERSION, useMetadata = true)
public class BuildHelper
{
	@Mod.Instance(Info.MODID)
	public static BuildHelper instance;
	
	@SidedProxy(serverSide = Info.SERVER_PROXY_CLASS, clientSide = Info.CLIENT_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{		
		Blocks.init();
		Items.init();
		
		Items.register();
		Blocks.register();
		
		proxy.preInit();
		proxy.registerRenders();
		
		TileEntities.register();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{		
		//register events for both the client and server (shared events)
		MinecraftForge.EVENT_BUS.register(EventsCommon.instance);
		
		proxy.init();
		
		Sounds.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) { }
}
