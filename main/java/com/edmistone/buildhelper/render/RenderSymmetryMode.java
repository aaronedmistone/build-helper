package com.edmistone.buildhelper.render;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import com.edmistone.buildhelper.items.ItemSymmetryTool.SymmetryMode;
import com.edmistone.buildhelper.operations.Render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/** Rendering for symmetry system 
 *  @author Aaron Edmistone */
public class RenderSymmetryMode
{
	/** Renders the symmetry lines based on the current symmetry mode of the player */
	public static void renderSymmetryMode(RenderWorldLastEvent event, Entity player, Vector3f accuratePos, Vector3f translation)
	{
		float symmetryLineDistance = 100;
		
		NBTTagCompound playerTags = player.getEntityData();
		float mx = playerTags.getFloat("SymmetryPosX");
		float my = playerTags.getFloat("SymmetryPosY");
		float mz = playerTags.getFloat("SymmetryPosZ");
		
		SymmetryMode symmetryMode = SymmetryMode.values()[playerTags.getInt("SymmetryMode")];
		boolean showNorthSouth =
				symmetryMode == SymmetryMode.NorthSouth || symmetryMode == SymmetryMode.Both;
		boolean showEastWest =
				symmetryMode == SymmetryMode.EastWest || symmetryMode == SymmetryMode.Both;
		
		if(mx == 0 && my == 0 && mz == 0)
			return;
		
		GlStateManager.pushMatrix();
		GlStateManager.translatef(-translation.x, -translation.y, -translation.z);
	    GlStateManager.enableRescaleNormal();
	    Tessellator tessellator = Tessellator.getInstance();
	    BufferBuilder vertexBuffer = tessellator.getBuffer();
		
		GlStateManager.disableTexture2D();
	    GlStateManager.disableLighting();
	    GlStateManager.enableBlend();
	    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    
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
	    
	    GlStateManager.disableBlend();
	    GlStateManager.enableLighting();
	    GlStateManager.enableTexture2D();
	    
	    GlStateManager.disableRescaleNormal();
	    GlStateManager.popMatrix();
	}
}
