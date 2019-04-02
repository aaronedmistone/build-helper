package com.edmistone.buildhelper.operations;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

//TODO: rework this, I should rework the seperation of enum and string; Instead a markdown type interpreter would be better.

/** Methods for sending chat messages */
public class Chat
{
	/** Send a simple chat message to the entity with the given color/style **/
	public static void send(Entity entity, String text, TextFormatting colorAndStyle)
	{
		entity.addChatMessage(new TextComponentString(colorAndStyle + text));
	}
	
	/** Send a simple chat message to the entity */
	public static void send(Entity entity, String text)
	{
		entity.addChatMessage(new TextComponentString(text));
	}
}
