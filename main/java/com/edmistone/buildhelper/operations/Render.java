package com.edmistone.buildhelper.operations;

import java.util.List;
import javax.vecmath.Vector3d;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

/** Contains methods for rendering shapes, lines, images, etc. 
 *  @author Aaron Edmistone */
public class Render
{
	/** Draws a line between the given points, with the given color (does not setup lighting, blending, etc.)*/
	public static void DrawLine(BufferBuilder vertexBuffer, Tessellator tessellator,
			Vector3d begin, Vector3d end,
			int R, int G, int B, int A)
	{
		DrawLine(vertexBuffer, tessellator, begin.x, begin.y, begin.z, end.x, end.y, end.z, R, G, B, A);
	}
	
	/** Draws a line between the given points, with the given color (does not setup lighting, blending, etc.)*/
	public static void DrawLine(BufferBuilder vertexbuffer, Tessellator tessellator,
			double startX, double startY, double startZ,
			double endX, double endY, double endZ,
			int R, int G, int B, int A)
	{
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(startX, startY, startZ).color(R, G, B, A).endVertex();
        vertexbuffer.pos(endX, endY, endZ).color(R, G, B, A).endVertex();
        tessellator.draw();
	}
	
	/** Draws a polygon (not filled) using the given points (not automatically closed)
	 *  with the given color (does not setup lighting, blending, etc.)*/
	public static void DrawPolygon(BufferBuilder vertexbuffer, Tessellator tessellator, List<Vector3d> points, int R, int G, int B, int A)
	{
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
		
		for(Vector3d point : points)
			vertexbuffer.pos(point.x, point.y, point.z).color(R, G, B, A).endVertex();
		
        tessellator.draw();
	}
}
