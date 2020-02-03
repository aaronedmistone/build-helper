package com.edmistone.buildhelper.items;

import java.util.List;

import javax.annotation.Nullable;

import com.edmistone.buildhelper.Info;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/** Build tool is currently only used for activating Build Helper mod blocks 
 * @author Aaron Edmistone */
public class ItemBuildTool extends Item
{
	public ItemBuildTool(String registryName)
	{
		super(new Properties().group(Info.TAB));
		this.setRegistryName(new ResourceLocation(Info.MODID, registryName));
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{		
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new StringTextComponent(TextFormatting.AQUA + I18n.format("build_tool.tooltip")));
	}
}