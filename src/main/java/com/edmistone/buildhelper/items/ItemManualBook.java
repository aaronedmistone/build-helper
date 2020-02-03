package com.edmistone.buildhelper.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;

public class ItemManualBook extends WrittenBookItem
{
   public ItemManualBook(Item.Properties builder)
   {
      super(builder);
   }

   public boolean hasEffect(ItemStack stack)
   {
      return false;
   }
}