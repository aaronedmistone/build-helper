package com.edmistone.buildhelper.registry;

import com.edmistone.buildhelper.Info;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

/** Initialisation and registration of all mod sounds 
 *  @author Aaron Edmistone */
public class Sounds
{
	public static SoundEvent BUILD_TOOL_USE;
	public static SoundEvent BUILD_TOOL_ROTATE;
	public static SoundEvent BUILD_TOOL_FAIL;
	public static SoundEvent SYMMETRY_TOOL_USE;

	public static void init()
	{
		BUILD_TOOL_USE = create("item.build_tool.use");
		BUILD_TOOL_ROTATE = create("item.build_tool.rotate");
		BUILD_TOOL_FAIL = create("item.build_tool.fail");
		SYMMETRY_TOOL_USE = create("item.symmetry_tool.use");
	}
	
	public static void register(IForgeRegistry<SoundEvent> registry)
	{
		init();
		
		registry.registerAll(
				BUILD_TOOL_USE,
				BUILD_TOOL_ROTATE,
				BUILD_TOOL_FAIL,
				SYMMETRY_TOOL_USE
				);
		
		Info.LOG.info("Sounds registered for Build Helper");
	}
	
	public static SoundEvent create(String registryName)
	{
		 SoundEvent result = new SoundEvent(new ResourceLocation(Info.MODID, registryName));
		 result.setRegistryName(registryName);
		 return result;
	}
}
