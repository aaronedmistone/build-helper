package com.edmistone.buildhelper.registry;

import com.edmistone.buildhelper.Info;
import com.edmistone.buildhelper.items.ItemBuildTool;
import com.edmistone.buildhelper.items.ItemSymmetryTool;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

/** Initialisation and registration of all mod items */
public class Items
{
	public static Item buildTool;
	public static Item symmetryTool;

	public static void init()
	{
		buildTool = new ItemBuildTool("build_tool", "build_tool");
		symmetryTool = new ItemSymmetryTool("symmetry_tool", "symmetry_tool");
	}

	public static void register()
	{
		registerItem(buildTool);
		registerItem(symmetryTool);
	}
	
	public static void registerRenders()
	{
		registerRender(buildTool);
		registerRender(symmetryTool);
	}

	public static void registerItem(Item item)
	{
		item.setCreativeTab(Info.TAB);
		GameRegistry.register(item);
		Info.LOG.info("Registered item for " + item.getUnlocalizedName().substring(5));
	}

	public static void registerRender(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(
				new ResourceLocation(Info.MODID, item.getUnlocalizedName().substring(5)), "inventory"));
		Info.LOG.info("Registered render for " + item.getUnlocalizedName().substring(5));
	}

	public static void registerRender(Item item, int meta, String fileName)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta,
				new ModelResourceLocation(new ResourceLocation(Info.MODID, fileName), "inventory"));
		Info.LOG.info("Registered render for " + item.getUnlocalizedName().substring(5));
	}

}
