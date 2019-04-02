package com.edmistone.buildhelper.events;

import javax.vecmath.Vector3f;

import com.edmistone.buildhelper.helpers.EntityHelper;
import com.edmistone.buildhelper.render.RenderSymmetryMode;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class EventRenderWorldLast
{
	/** Fired during renderWorldPass at the end, before the hand is rendered */
	public static void onRenderWorldLast(RenderWorldLastEvent event)
	{
		Entity player = Minecraft.getMinecraft().thePlayer;
		Vector3f accuratePosition = EntityHelper.getTruePosition(player, event.getPartialTicks());
		Vector3f translation = new Vector3f();
		translation.x = accuratePosition.x - 0.5f;
		translation.y = accuratePosition.y - 0.005f;
		translation.z = accuratePosition.z - 0.5f;
		
		RenderSymmetryMode.renderSymmetryMode(event, player, accuratePosition, translation);
	}
}
