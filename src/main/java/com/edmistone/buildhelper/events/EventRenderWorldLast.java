package com.edmistone.buildhelper.events;

import com.edmistone.buildhelper.render.RenderCopyArea;
import com.edmistone.buildhelper.render.RenderPasteArea;
import com.edmistone.buildhelper.render.RenderSymmetryMode;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/** @author Aaron Edmistone */
public class EventRenderWorldLast
{
	/** Fired during renderWorldPass at the end, before the hand is rendered */
	public static void onRenderWorldLast(RenderWorldLastEvent event)
	{
		Minecraft instance = Minecraft.getInstance();
		Entity player = instance.player;
		Vec3d projectedView = instance.gameRenderer.getActiveRenderInfo().getProjectedView();
		RenderCopyArea.renderCopyArea(event, player, projectedView);
		RenderPasteArea.renderPasteArea(event, player, projectedView);
		RenderSymmetryMode.renderSymmetryMode(event, player, projectedView);
	}
}
