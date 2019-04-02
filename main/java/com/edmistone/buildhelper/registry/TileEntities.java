package com.edmistone.buildhelper.registry;

import com.edmistone.buildhelper.Info;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

/** Registration of all mod tile entities */
public class TileEntities 
{
	public static void register()
	{
		//TODO: Add mod tile entities
		//registerTileEntity(SomeTileEntityClass, "some_tile_entity_registry_name");
	}
	
	@SuppressWarnings("unused")
	private static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String registryName)
	{
		GameRegistry.registerTileEntity(
				tileEntityClass, Info.MODID + ":" + registryName);
	}
}
