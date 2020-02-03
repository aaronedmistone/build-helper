package com.edmistone.buildhelper.tabs;

import com.edmistone.buildhelper.registry.Items;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/** Main mod tab for creative mode 
 *  @author Aaron Edmistone */
public class TabMain extends ItemGroup
{
	public TabMain()
	{
		super("buildhelpertab");
	}
	
	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(Items.buildTool);
	}
}
