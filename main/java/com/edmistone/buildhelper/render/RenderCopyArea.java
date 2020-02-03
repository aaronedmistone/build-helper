package com.edmistone.buildhelper.render;

import java.util.SortedMap;
import java.util.TreeMap;

import org.lwjgl.opengl.GL11;

import com.edmistone.buildhelper.helpers.TagHelper;
import com.edmistone.buildhelper.operations.Render;

import net.minecraft.client.renderer.BufferBuilder;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/** Rendering for copy blocks area
 *  @author Aaron Edmistone */
public class RenderCopyArea
{
	/** Renders the copy area if the player has active copy blocks */
	public static void renderCopyArea(RenderWorldLastEvent event, Entity player, Vec3d projectedView)
	{		
		CompoundNBT playerTags = player.getPersistentData();
		ListNBT copyBlocks = playerTags.getList("CopyBlocks", 10);
		
		if(copyBlocks == null || copyBlocks.toArray().length < 2)
			return;
		
		BlockPos currentBlockPos = TagHelper.ReadBlockPos(copyBlocks.getCompound(0));
		float mx = -currentBlockPos.getX();
		float my = currentBlockPos.getY() + 1.1f;
		float mz = -currentBlockPos.getZ();
		
		BlockPos partnerBlockPos = TagHelper.ReadBlockPos(copyBlocks.getCompound(1));
		float px = -partnerBlockPos.getX();
		float py = partnerBlockPos.getY() + 1.1f;
		float pz = -partnerBlockPos.getZ();
		
		if(py < my)
			my = py;
		
		if(Math.abs(my - player.getPosY()) > 100)
			return;
		
		if(mx < px) px--; else mx--;
		if(mz < pz) pz--; else mz--;
		
		BlockPos[] corners = new BlockPos[]
		{
				new BlockPos(currentBlockPos.getX(), my, partnerBlockPos.getZ()),
				new BlockPos(partnerBlockPos.getX(), my, currentBlockPos.getZ()),
				new BlockPos(currentBlockPos.getX(), my, currentBlockPos.getZ()),
				new BlockPos(partnerBlockPos.getX(), my, partnerBlockPos.getZ())	
		};
		
		SortedMap<Double, BlockPos> cornersByCornerDistSq = new TreeMap<Double,BlockPos>();
		cornersByCornerDistSq.put(corners[0].distanceSq(player.getPosition()), corners[0]);
		cornersByCornerDistSq.put(corners[1].distanceSq(player.getPosition())+0.0001, corners[1]);
		cornersByCornerDistSq.put(corners[2].distanceSq(player.getPosition())+0.0002, corners[2]);
		cornersByCornerDistSq.put(corners[3].distanceSq(player.getPosition())+0.0003, corners[3]);
		
		double closestCornerDistSq = cornersByCornerDistSq.firstKey();
		double secondClosestCornerDistSq = cornersByCornerDistSq.keySet().toArray(new Double[cornersByCornerDistSq.keySet().size()])[1];
		
		Boolean isHorizontal = cornersByCornerDistSq.get(closestCornerDistSq).getZ() == cornersByCornerDistSq.get(secondClosestCornerDistSq).getZ();
		//Boolean isLower = isHorizontal ? (player.getPosX() < cornersByCornerDistSq.get(closestCornerDistSq).getX()) : (player.getPosZ() < cornersByCornerDistSq.get(closestCornerDistSq).getZ());
		
		
		BlockPos wallPoint = cornersByCornerDistSq.get(closestCornerDistSq);
		wallPoint = wallPoint.add(isHorizontal ? player.getPosX() - wallPoint.getX() : 0, 0, isHorizontal ? 0 : player.getPosZ() - wallPoint.getZ());
		double distance = MathHelper.sqrt(wallPoint.distanceSq(player.getPosition().add(0,wallPoint.getY() - player.getPosYEye(),0)));
		
		
		Tessellator tessellator = Tessellator.getInstance();
	    BufferBuilder vertexBuffer = tessellator.getBuffer();
		
		RenderSystem.pushMatrix();
		RenderSystem.rotatef(player.rotationPitch, 1,0,0);
		RenderSystem.rotatef(player.rotationYaw, 0,1,0);
		RenderSystem.translated(projectedView.x, -projectedView.y, projectedView.z);
		RenderSystem.enableRescaleNormal();
	    RenderSystem.disableTexture();
	    RenderSystem.disableLighting();
	    RenderSystem.enableBlend();
	    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
	    for(float i = 0f; i < 50f; i+=0.5f)
	    {
	    	int alpha = (int)MathHelper.clamp((255f - distance*20) - (i * 10f) + (player.getPosYEye() - my) * 10, 0, 255);
	    	
    		Render.DrawLine(
    				vertexBuffer, tessellator,
    				mx, my + i, mz,
    				px, my + i, mz,
    				0,255,255,alpha);
    	
    		Render.DrawLine(
    				vertexBuffer, tessellator,
    				mx, my + i, mz,
    				mx, my + i, pz,
    				0,255,255,alpha);
    		
    		Render.DrawLine(
    				vertexBuffer, tessellator,
    				px, my + i, pz,
    				mx, my + i, pz,
    				0,255,255,alpha);
    		
    		Render.DrawLine(
    				vertexBuffer, tessellator,
    				px, my + i, pz,
    				px, my + i, mz,
    				0,255,255,alpha);
	    }
	    
	    RenderSystem.disableBlend();
	    RenderSystem.enableLighting();
	    RenderSystem.enableTexture();
	    
	    RenderSystem.disableRescaleNormal();
	    RenderSystem.popMatrix();
	}
}
