package com.edmistone.buildhelper;

import com.edmistone.buildhelper.proxy.CommonProxy;
import com.edmistone.buildhelper.proxy.ClientProxy;
import com.edmistone.buildhelper.proxy.ServerProxy;
import com.edmistone.buildhelper.registry.Blocks;
import com.edmistone.buildhelper.registry.Items;
import com.edmistone.buildhelper.registry.Sounds;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/** Main mod class 
 *  @author Aaron Edmistone */
@Mod("buildhelper")
public class BuildHelper
{
	// test comment
	public static BuildHelper instance;
	public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
	
	public BuildHelper()
	{
		instance = this;
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
	}
	
	private void init(FMLCommonSetupEvent event)
	{
		proxy.hookEvents();
	}
	
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
    	@SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> registryEvent)
        {
        	Blocks.register(registryEvent.getRegistry());
        }
    	
    	@SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> registryEvent)
        {
        	Items.register(registryEvent.getRegistry());
        }
    	
    	@SubscribeEvent
        public static void onSoundsRegistry(final RegistryEvent.Register<SoundEvent> registryEvent)
        {
    		Sounds.register(registryEvent.getRegistry());
        }
    }
}
