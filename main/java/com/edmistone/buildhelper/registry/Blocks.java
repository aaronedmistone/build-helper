package com.edmistone.buildhelper.registry;

import java.util.HashMap;
import java.util.Map;

import com.edmistone.buildhelper.Info;
import com.edmistone.buildhelper.blocks.BlockCopyBlock;
import com.edmistone.buildhelper.blocks.BlockPasteBlock;
import com.edmistone.buildhelper.blocks.BlockPasteVariantBlock;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.IForgeRegistry;

/** Initialisation and registration of all mod blocks 
 *  @author Aaron Edmistone */
public class Blocks
{
	public static Map<Block, BlockItem> BLOCK_ITEMS = new HashMap<Block, BlockItem>();
	
	public static Block copyBlock;
	public static Block pasteBlock;
	public static Block pasteVariantBlock;

	public static void init()
	{		
		copyBlock = new BlockCopyBlock("copy_block");
		pasteBlock = new BlockPasteBlock("paste_block");
		pasteVariantBlock = new BlockPasteVariantBlock("paste_variant_block");
		
		map(copyBlock, Info.TAB);
		map(pasteBlock, Info.TAB);
		map(pasteVariantBlock, Info.TAB);
	}

	public static void register(IForgeRegistry<Block> registry)
	{
		init();
		
		registry.registerAll(
				copyBlock,
				pasteBlock,
				pasteVariantBlock);
		
		
		Info.LOG.info("Registered blocks for Build Helper");
	}
	
	public static void map(Block block, ItemGroup tab)
	{
		BLOCK_ITEMS.put(block, (BlockItem) new BlockItem(block, new Item.Properties().group(tab)).setRegistryName(block.getRegistryName()));
	}
}