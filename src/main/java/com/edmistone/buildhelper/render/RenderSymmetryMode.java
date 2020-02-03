package com.edmistone.buildhelper.render;

import org.lwjgl.opengl.GL11;

import com.edmistone.buildhelper.items.ItemSymmetryTool.SymmetryMode;
import com.edmistone.buildhelper.operations.Render;

import net.minecraft.client.renderer.BufferBuilder;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/** Rendering for symmetry system 
 *  @author Aaron Edmistone */
public class RenderSymmetryMode
{
	/** Renders the symmetry lines based on the current symmetry mode of the player */
	public static void renderSymmetryMode(RenderWorldLastEvent event, Entity player, Vec3d projectedView)
	{
		float symmetryLineDistance = 100;
		
		CompoundNBT playerTags = player.getPersistentData();
		float mx = -playerTags.getFloat("SymmetryPosX");
		float my = playerTags.getFloat("SymmetryPosY");
		float mz = -playerTags.getFloat("SymmetryPosZ");
		
		SymmetryMode symmetryMode = SymmetryMode.values()[playerTags.getInt("SymmetryMode")];
		boolean showNorthSouth =
				symmetryMode == SymmetryMode.NorthSouth || symmetryMode == SymmetryMode.Both;
		boolean showEastWest =
				symmetryMode == SymmetryMode.EastWest || symmetryMode == SymmetryMode.Both;
		
		if(mx == 0 && my == 0 && mz == 0)
			return;
		
		Tessellator tessellator = Tessellator.getInstance();
	    BufferBuilder vertexBuffer = tessellator.getBuffer();
		
		RenderSystem.pushMatrix();
		RenderSystem.rotatef(player.rotationPitch, 1,0,0);
		RenderSystem.rotatef(player.rotationYaw, 0,1,0);
		RenderSystem.translated(projectedView.x - 0.5f, -projectedView.y, projectedView.z - 0.5f);
		RenderSystem.enableRescaleNormal();
	    RenderSystem.disableTexture();
	    RenderSystem.disableLighting();
	    RenderSystem.enableBlend();
	    RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    
	    for(float i = -5f; i < 5f; i+=0.05f)
	    {
	    	if(showNorthSouth)
	    	{
	    		Render.DrawLine(
	    				vertexBuffer, tessellator,
	    				mx + symmetryLineDistance, my + i, mz,
	    				mx - symmetryLineDistance, my + i, mz,
	    				255,0,0,(int)(100f - (MathHelper.abs(i) * 20f)));
	    	}
	        
	        if(showEastWest)
	        {
	        	Render.DrawLine(
	    				vertexBuffer, tessellator,
	    				mx, my + i, mz - symmetryLineDistance,
	    				mx, my + i, mz + symmetryLineDistance,
	    				0,0,255,(int)(100f - (MathHelper.abs(i) * 20f)));
	        }
	    }
	    
	    RenderSystem.disableBlend();
	    RenderSystem.enableLighting();
	    RenderSystem.enableTexture();
	    
	    RenderSystem.disableRescaleNormal();
	    RenderSystem.popMatrix();
	}
}
