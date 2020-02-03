package com.edmistone.buildhelper.render;

import java.util.SortedMap;
import java.util.TreeMap;

import org.lwjgl.opengl.GL11;

import com.edmistone.buildhelper.helpers.MutableBoundingBoxHelper;
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
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/** Rendering for paste block area
 *  @author Aaron Edmistone */
public class RenderPasteArea
{
	/** Renders the paste area if the player has an active paste block and active copy blocks */
	public static void renderPasteArea(RenderWorldLastEvent event, Entity player, Vec3d projectedView)
	{		
		CompoundNBT playerTags = player.getPersistentData();
		ListNBT copyBlocks = playerTags.getList("CopyBlocks", 10);
		if(copyBlocks == null || copyBlocks.toArray().length < 2)
			return;
		
		ListNBT pasteBlocks = playerTags.getList("PasteBlocks", 10);
		if(pasteBlocks == null || pasteBlocks.toArray().length < 1)
			return;
		
		BlockPos currentBlockPos = TagHelper.ReadBlockPos(copyBlocks.getCompound(0));
		BlockPos partnerBlockPos = TagHelper.ReadBlockPos(copyBlocks.getCompound(1));
		BlockPos pasteBlockPos = TagHelper.ReadBlockPos(pasteBlocks.getCompound(0));
		
		MutableBoundingBox sourceBoundingBox = MutableBoundingBoxHelper.reduceBoundingBox(new MutableBoundingBox(currentBlockPos, partnerBlockPos.add(0,50,0)));
		MutableBoundingBox destinationBoundingBox = new MutableBoundingBox(pasteBlockPos, pasteBlockPos.add(sourceBoundingBox.getLength()));
		BlockPos sourceDestinationDifference = new BlockPos(
				destinationBoundingBox.minX - sourceBoundingBox.minX,
				destinationBoundingBox.minY - sourceBoundingBox.minY,
				destinationBoundingBox.minZ - sourceBoundingBox.minZ);
		
		currentBlockPos = currentBlockPos.add(sourceDestinationDifference);
		partnerBlockPos = partnerBlockPos.add(sourceDestinationDifference);
		
		float cx = -(pasteBlockPos.getX());
		float cy = pasteBlockPos.getY() + 0.01f;
		float cz = -pasteBlockPos.getZ()-1f;
		
		float mx = -currentBlockPos.getX();
		float my = currentBlockPos.getY() + 1.1f;
		float mz = -currentBlockPos.getZ();
		
		float px = -partnerBlockPos.getX();
		float py = partnerBlockPos.getY() + 1.1f;
		float pz = -partnerBlockPos.getZ();
		
		if(py < my)
			my = py;
		
		if(mx < px) px--; else mx--;
		if(mz < pz) pz--; else mz--;
		
		if(Math.abs(my - player.getPosY()) > 100)
			return;
		
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
    				249,159,0,alpha);
    	
    		Render.DrawLine(
    				vertexBuffer, tessellator,
    				mx, my + i, mz,
    				mx, my + i, pz,
    				249,159,0,alpha);
    		
    		Render.DrawLine(
    				vertexBuffer, tessellator,
    				px, my + i, pz,
    				mx, my + i, pz,
    				249,159,0,alpha);
    		
    		Render.DrawLine(
    				vertexBuffer, tessellator,
    				px, my + i, pz,
    				px, my + i, mz,
    				249,159,0,alpha);
	    }
	    
	    RenderSystem.lineWidth(10);
	    int chestLineAlpha = (int)MathHelper.clamp((255f - distance*20), 0, 255);
	    Render.DrawLine(vertexBuffer, tessellator, cx, cy, cz, cx + 1, cy, cz, 255, 255, 0, chestLineAlpha);
	    Render.DrawLine(vertexBuffer, tessellator, cx + 1, cy, cz, cx + 1, cy, cz + 1, 255, 255, 0, chestLineAlpha);
	    Render.DrawLine(vertexBuffer, tessellator, cx + 1, cy, cz + 1, cx, cy, cz + 1, 255, 255, 0, chestLineAlpha);
	    Render.DrawLine(vertexBuffer, tessellator, cx, cy, cz + 1, cx, cy, cz, 255, 255, 0, chestLineAlpha);
	    RenderSystem.lineWidth(1);
	    
	    RenderSystem.disableBlend();
	    RenderSystem.enableLighting();
	    RenderSystem.enableTexture();
	    
	    RenderSystem.disableRescaleNormal();
	    RenderSystem.popMatrix();
	}
}
