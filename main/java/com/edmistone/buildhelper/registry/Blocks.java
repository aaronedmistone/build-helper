package com.edmistone.buildhelper.registry;

import com.edmistone.buildhelper.Info;
import com.edmistone.buildhelper.blocks.BlockCopyBlock;
import com.edmistone.buildhelper.blocks.BlockPasteBlock;
import com.edmistone.buildhelper.blocks.BlockPasteVariantBlock;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

/** Initialisation and registration of all mod blocks */
public class Blocks
{
	public static Block copyBlock;
	public static Block pasteBlock;
	public static Block pasteVariantBlock;

	public static void init()
	{
		copyBlock = new BlockCopyBlock("copy_block");
		pasteBlock = new BlockPasteBlock("paste_block");
		pasteVariantBlock = new BlockPasteVariantBlock("paste_variant_block");
	}

	public static void register()
	{
		registerBlock(copyBlock);
		registerBlock(pasteBlock);
		registerBlock(pasteVariantBlock);
	}

	public static void registerRenders()
	{
		registerRender(copyBlock);
		registerRender(pasteBlock);
		registerRender(pasteVariantBlock);
	}

	public static void registerBlock(Block block)
	{
		block.setCreativeTab(Info.TAB);
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		Info.LOG.info("Registered block for " + block.getUnlocalizedName().substring(5));
	}

	public static void registerBlock(Block block, ItemBlock itemBlock)
	{
		block.setCreativeTab(Info.TAB);
		GameRegistry.register(block);
		GameRegistry.register(itemBlock.setRegistryName(block.getRegistryName()));
		Info.LOG.info("Registered block for " + block.getUnlocalizedName().substring(5));
	}
	
	public static void registerRender(Block block)
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(
				new ResourceLocation(Info.MODID, block.getUnlocalizedName().substring(5)), "inventory"));
		Info.LOG.info("Registered render for " + block.getUnlocalizedName().substring(5));
	}

	public static void registerRender(Block block, int meta, String fileName)
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta,
				new ModelResourceLocation(new ResourceLocation(Info.MODID, fileName), "inventory"));
		Info.LOG.info("Registered render for " + block.getUnlocalizedName().substring(5));
	}

}