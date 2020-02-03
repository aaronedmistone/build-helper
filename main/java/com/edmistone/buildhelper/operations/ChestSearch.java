package com.edmistone.buildhelper.operations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.edmistone.buildhelper.helpers.BlockHelper;
import com.edmistone.buildhelper.helpers.MutableBoundingBoxHelper;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.state.properties.BedPart;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

/** Functions for calculation of various things 
 *  @author Aaron Edmistone */
public class ChestSearch
{
	//TODO: tidy this up with a for loop instead of foreach anon function and a lot of abstraction
	private static int _bookPageLineIterator = 0;
	private static String _bookCurrentPageText = "";
	/** Checks all of the items in the chest at the given location
	 * returns true if chest contains all necessary items, false otherwise
	 * Optionally, gives the player a book with all missing items listed.
	 * 
	 * @param giveBook - Whether or not to give the player a book with the missing items
	 * @param world
	 * @param player
	 * @param chestPos - The blockpos position of the chest to check
	 * @param sourceBlockPosStart
	 * @param sourceBlockPosEnd
	 * @param destinationBlockPos
	 * @return
	 */
	public static Boolean containsItemsForClone(Boolean giveBook, World world, PlayerEntity player, BlockPos chestPos, BlockPos sourceBlockPosStart, BlockPos sourceBlockPosEnd, BlockPos destinationBlockPos)
	{
		if(player.isCreative())
			return true;
		
		MutableBoundingBox sourceBoundingBox = MutableBoundingBoxHelper.reduceBoundingBox(new MutableBoundingBox(sourceBlockPosStart, sourceBlockPosEnd));
		MutableBoundingBox destinationBoundingBox = new MutableBoundingBox(destinationBlockPos, destinationBlockPos.add(sourceBoundingBox.getLength()));
        
        int totalBlocks = sourceBoundingBox.getXSize() * sourceBoundingBox.getYSize() * sourceBoundingBox.getZSize();

        if (totalBlocks > 32768)
        	return false;
        
        IInventory inventory = HopperTileEntity.getInventoryAtPosition(world, chestPos);
        if(inventory == null)
        {
        	Chat.send(player, "You need a chest full of necessary blocks attached to the paste block before pasting...");
        	return false;
        }
        
        Map<String,Integer> missingItems = new HashMap<String,Integer>();
        Map<String,Item> missingStringToItem = new HashMap<String,Item>();
        
        BlockPos sourceDestinationDifference = new BlockPos(
				destinationBoundingBox.minX - sourceBoundingBox.minX,
				destinationBoundingBox.minY - sourceBoundingBox.minY,
				destinationBoundingBox.minZ - sourceBoundingBox.minZ);

		for (int z = sourceBoundingBox.minZ; z <= sourceBoundingBox.maxZ; ++z)
		{
		    for (int y = sourceBoundingBox.minY; y <= sourceBoundingBox.maxY; ++y)
		    {
		        for (int x = sourceBoundingBox.minX; x <= sourceBoundingBox.maxX; ++x)
		        {
		            BlockPos currentSourceBlock = new BlockPos(x, y, z);
		            BlockPos currentDestinationBlock = currentSourceBlock.add(sourceDestinationDifference);
		            BlockState currentBlockState = world.getBlockState(currentSourceBlock);
		            
		            // somemod:ruby_block is able to be replaced with minecraft:ruby_block and vice versa
		            if(world.getBlockState(currentDestinationBlock).getBlock().getRegistryName().getPath() == currentBlockState.getBlock().getRegistryName().getPath())
		            	continue;
		            
		            Block currentBlock = currentBlockState.getBlock();
		            boolean isFootPartOfBed = currentBlockState.has(BlockStateProperties.BED_PART) && currentBlockState.get(BlockStateProperties.BED_PART) == BedPart.FOOT;
		            if(isFootPartOfBed)
		            	continue;
		            
		            Item blockItem = BlockHelper.BlockToItem(currentBlock);
		            if(blockItem == null)
		            	continue;
		            
		            ItemStack stack = blockItem.getDefaultInstance();
		            if(stack == null)
		            	continue;
		            
		            ITextComponent nameText = blockItem.getDisplayName(stack);
		            if(nameText == null)
		            	continue;
		            
		            String blockItemName = nameText.getUnformattedComponentText();
		            
		            if(blockItem != null)
		            {
			            if (currentBlock != net.minecraft.block.Blocks.AIR)
			            {
			                if(!missingItems.containsKey(blockItemName))
	                		{
	                			missingItems.put(blockItemName, 1);
	                			missingStringToItem.put(blockItemName, blockItem);
	                		}
	                		else
	                		{
	                			missingItems.put(blockItemName, missingItems.get(blockItemName) + 1);
	                		}
			            }
		            }
		        }
		    }
		}
		
		Map<String,Integer> itemsToRemove = new HashMap<String,Integer>(missingItems);
		String[] missingItemKeys = missingItems.keySet().toArray(new String[missingItems.keySet().size()]);
		for(int i = missingItemKeys.length-1; i >= 0; i--)
		{
			int need = missingItems.get(missingItemKeys[i]);
			int have = inventory.count(missingStringToItem.get(missingItemKeys[i]));
			
			if(have >= need)
			{
				missingItems.remove(missingItemKeys[i]);
			}
			else if(have > 0)
			{
				missingItems.put(missingItemKeys[i], missingItems.get(missingItemKeys[i]) - have);
			}
		}
		
		Boolean result = missingItems.keySet().size() == 0;
		
		if(result)
		{
			String[] itemsToRemoveKeys = itemsToRemove.keySet().toArray(new String[itemsToRemove.keySet().size()]);
			for(int i = itemsToRemoveKeys.length-1; i >= 0; i--)
			{
				int need = itemsToRemove.get(itemsToRemoveKeys[i]);
				for(int t = 0; t < inventory.getSizeInventory(); t++)
				{
					ItemStack currentStack = inventory.getStackInSlot(t);
					Item currentItem = currentStack.getItem();
					String currentName = currentItem.getDisplayName(currentItem.getDefaultInstance()).getUnformattedComponentText();
					if(currentName.equals(itemsToRemoveKeys[i]))
					{
						if(currentStack.getCount() < need)
						{
							need -= currentStack.getCount();
							inventory.decrStackSize(t, currentStack.getCount());
						}
						else
						{
							inventory.decrStackSize(t, need);
							need = 0;
						}
					}
					
					if(need == 0)
						break;
				}
			}
		}
		
		if(!result && giveBook)
		{
			List<String> bookPages = Lists.newArrayList();
			_bookCurrentPageText = "(BuildHelper) Missing:\n";
			_bookPageLineIterator = 2;
			FontRenderer font = Minecraft.getInstance().fontRenderer;
			missingItems.forEach((key, value) ->
			{
				if(bookPages.size() > 10)
					return;
				
				String entry = String.format("%d x %s", value, key);
				if(font.getStringWidth(entry) > 115)
					entry = font.trimStringToWidth(entry, 115 - font.getStringWidth("..")) + "..";
				
				_bookCurrentPageText += entry;
				
				if(_bookPageLineIterator == 14)
				{
					_bookPageLineIterator = 1;
					bookPages.add("\"" + _bookCurrentPageText + "\"");
					_bookCurrentPageText = "";
					if(bookPages.size() > 10)
						bookPages.add("\"Woah!\n\nMore than 10 pages!?\n\nSo many items missing!\n\nCome on I'm not going to list them all...\"");
				}
				else
				{
					_bookCurrentPageText += "\n";
				}
			});
			if(_bookCurrentPageText != "")
				bookPages.add("\"" + _bookCurrentPageText + "\"");
			
			ItemStack missingItemsBook = new ItemStack(Items.WRITTEN_BOOK, 1);
			ListNBT listnbt = new ListNBT();
			CompoundNBT tag = new CompoundNBT();
			
			for(int i = 1; i <= bookPages.size(); i++)
			{
				listnbt.add(StringNBT.valueOf(bookPages.get(i-1)));
			}
			
			tag.put("pages", listnbt);
			tag.put("author", StringNBT.valueOf(player.getGameProfile().getName()));
			tag.put("title", StringNBT.valueOf("Missing Parts"));
			
			missingItemsBook.setTag(tag);
			
			player.addItemStackToInventory(missingItemsBook);
		}
		return result;
	}
}
