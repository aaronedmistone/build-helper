package com.edmistone.buildhelper.items;

import java.util.List;
import com.edmistone.buildhelper.Info;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

/** Build tool is currently only used for activating Build Helper mod blocks */
public class ItemBuildTool extends Item
{
	public ItemBuildTool(String unlocalizedName, String registryName)
	{
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName(new ResourceLocation(Info.MODID, registryName));
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{		
		super.addInformation(stack, playerIn, tooltip, advanced);
		tooltip.add(TextFormatting.AQUA + I18n.format("build_tool.tooltip"));
	}
}