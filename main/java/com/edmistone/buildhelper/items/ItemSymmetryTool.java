package com.edmistone.buildhelper.items;

import java.util.List;

import com.edmistone.buildhelper.Info;
import com.edmistone.buildhelper.operations.Chat;
import com.edmistone.buildhelper.registry.Sounds;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/** Symmetry tool is used to place lines of symmetry in the world
 *  that can be used to build multiple blocks at once */
public class ItemSymmetryTool extends Item
{
	public ItemSymmetryTool(String unlocalizedName, String registryName)
	{
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName(new ResourceLocation(Info.MODID, registryName));
	}
	
	public enum SymmetryMode
	{
		None,
		EastWest,
		NorthSouth,
		Both
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand)
	{		
		NBTTagCompound playerTags = player.getEntityData();
		int newMode = playerTags.getInteger("SymmetryMode") + 1;
		
		if(newMode >= SymmetryMode.values().length)
			newMode = 0;
		
		playerTags.setInteger("SymmetryMode", newMode);
		playerTags.setFloat("SymmetryPosX", newMode == 0 ? 0 : MathHelper.floor_float((float) player.posX));
		playerTags.setFloat("SymmetryPosY", newMode == 0 ? 0 : MathHelper.floor_float((float) player.posY));
		playerTags.setFloat("SymmetryPosZ", newMode == 0 ? 0 : MathHelper.floor_float((float) player.posZ));
		
		player.writeToNBTAtomically(playerTags);
		
		if(world.isRemote)
		{
			world.playSound(
					player,
					player.getPosition(),
					Sounds.SYMMETRY_TOOL_USE,
					SoundCategory.PLAYERS,
					0.5F,
					world.rand.nextFloat() * 0.1F + 0.9F);
			
			Chat.send(player, "Symmetry mode set to " + SymmetryMode.values()[newMode].name());
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack); 
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		tooltip.add(TextFormatting.AQUA + I18n.format("symmetry_tool.tooltip"));
	}
}