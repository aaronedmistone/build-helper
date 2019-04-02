package com.edmistone.buildhelper.tabs;

import com.edmistone.buildhelper.registry.Items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/** Main mod tab for creative mode */
public class TabMain extends CreativeTabs
{
	public TabMain()
	{
		super("buildhelpertab");
	}
	
	@Override
	public Item getTabIconItem()
	{
		return Items.buildTool;
	}
}
