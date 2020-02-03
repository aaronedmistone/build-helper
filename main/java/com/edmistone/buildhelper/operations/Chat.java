package com.edmistone.buildhelper.operations;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

//TODO: also, having this support placing the chat elsewhere will be very handy for bulk repositioning of mod chat messages

/** Methods for sending chat messages with support for MC-HTML Chat Markup
 *  @author Aaron Edmistone*/
public class Chat
{
	// MC-HTML Chat Markup - (Color and Reset both reset the formatting)
	private static final Map<String, TextFormatting> textFormattingByMcHtml = ImmutableMap.<String, TextFormatting>builder()
			.put("<Black>", TextFormatting.BLACK)
			.put("<DarkBlue>", TextFormatting.DARK_BLUE)
			.put("<DarkGreen>", TextFormatting.DARK_GREEN)
			.put("<DarkAqua>", TextFormatting.DARK_AQUA)
			.put("<DarkRed>", TextFormatting.DARK_RED)
			.put("<DarkPurple>", TextFormatting.DARK_PURPLE)
			.put("<Gold>", TextFormatting.GOLD)
			.put("<Gray>", TextFormatting.GRAY)
			.put("<DarkGray>", TextFormatting.DARK_GRAY)
			.put("<Blue>", TextFormatting.BLUE)
			.put("<Green>", TextFormatting.GREEN)
			.put("<Aqua>", TextFormatting.AQUA)
			.put("<Red>", TextFormatting.RED)
			.put("<LightPurple>", TextFormatting.LIGHT_PURPLE)
			.put("<Yellow>", TextFormatting.YELLOW)
			.put("<White>", TextFormatting.WHITE)
			.put("<~>", TextFormatting.OBFUSCATED)
			.put("<b>", TextFormatting.BOLD)
			.put("<s>", TextFormatting.STRIKETHROUGH)
			.put("<u>", TextFormatting.UNDERLINE)
			.put("<i>", TextFormatting.ITALIC)
			.put("</>", TextFormatting.RESET)
			.build();
	
	/** Send a simple chat message to the entity (supports mc-html chat markup) */
	public static void send(Entity entity, String text)
	{
		for(Map.Entry<String, TextFormatting> entry : textFormattingByMcHtml.entrySet())
		{
			text = text.replace(entry.getKey(), entry.getValue().toString());
		}
		entity.sendMessage(new StringTextComponent(text));
	}
}
