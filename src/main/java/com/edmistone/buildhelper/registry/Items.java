package com.edmistone.buildhelper.registry;

import com.edmistone.buildhelper.Info;
import com.edmistone.buildhelper.items.ItemBuildTool;
import com.edmistone.buildhelper.items.ItemSymmetryTool;

import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraftforge.registries.IForgeRegistry;

/** Initialisation and registration of all mod items 
 *  @author Aaron Edmistone */
public class Items
{
	public static Item buildTool;
	public static Item symmetryTool;

	public static void init()
	{
		buildTool = new ItemBuildTool("build_tool");
		symmetryTool = new ItemSymmetryTool("symmetry_tool");
	}

	public static void register(IForgeRegistry<Item> registry)
	{
		init();
		
		for (BlockItem value : Blocks.BLOCK_ITEMS.values())
		{
		    registry.register(value);
		}
		
		registry.registerAll(
				buildTool,
				symmetryTool);
		
		Info.LOG.info("Registered items for Build Helper");
	}
}
