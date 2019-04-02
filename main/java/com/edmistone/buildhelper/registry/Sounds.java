package com.edmistone.buildhelper.registry;

import com.edmistone.buildhelper.Info;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/** Initialisation and registration of all mod sounds */
public class Sounds
{
	public static SoundEvent BUILD_TOOL_USE;
	public static SoundEvent BUILD_TOOL_ROTATE;
	public static SoundEvent BUILD_TOOL_FAIL;
	public static SoundEvent SYMMETRY_TOOL_USE;

	public static void init()
	{
		BUILD_TOOL_USE = register("item.build_tool.use");
		BUILD_TOOL_ROTATE = register("item.build_tool.rotate");
		BUILD_TOOL_FAIL = register("item.build_tool.fail");
		SYMMETRY_TOOL_USE = register("item.symmetry_tool.use");
	}

	public static SoundEvent register(String registryName)
	{
		ResourceLocation location = new ResourceLocation(Info.MODID, registryName);
		SoundEvent result = new SoundEvent(location);

		SoundEvent.REGISTRY.register(SoundEvent.REGISTRY.getKeys().size(), location, result);
		Info.LOG.info("Sound registered for " + registryName);
		
		return result;
	}
}
