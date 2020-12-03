package com.mumfrey.worldeditcui.render.shapes;

import com.mumfrey.worldeditcui.render.LineStyle;
import com.mumfrey.worldeditcui.render.RenderStyle;
import com.mumfrey.worldeditcui.render.points.PointCube;
import com.mumfrey.worldeditcui.util.Vector3;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.lwjgl.opengl.GL11;

/**
 * Draws the grid lines around a cylindrical region
 * 
 * @author yetanotherx
 * @author Adam Mummery-Smith
 */
public class RenderCylinderGrid extends RenderRegion
{
	private final double radX;
	private final double radZ;
	private final int minY;
	private final int maxY;
	private final double centreX;
	private final double centreZ;
	
	public RenderCylinderGrid(RenderStyle style, PointCube centre, double radX, double radZ, int minY, int maxY)
	{
		super(style);
		this.radX = radX;
		this.radZ = radZ;
		this.minY = minY;
		this.maxY = maxY;
		this.centreX = centre.getPoint().getX() + 0.5;
		this.centreZ = centre.getPoint().getZ() + 0.5;
	}
	
	@Override
	public void render(Vector3 cameraPos)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuffer();
		
		double xPos = this.centreX - cameraPos.getX();
		double zPos = this.centreZ - cameraPos.getZ();

		for (LineStyle line : this.style.getLines())
		{
			if (!line.prepare(this.style.getRenderType()))
			{
				continue;
			}
			
			int tmaxY = this.maxY + 1;
			int tminY = this.minY;
			int posRadiusX = (int)Math.ceil(this.radX);
			int negRadiusX = (int)-Math.ceil(this.radX);
			int posRadiusZ = (int)Math.ceil(this.radZ);
			int negRadiusZ = (int)-Math.ceil(this.radZ);
			
			for (double tempX = negRadiusX; tempX <= posRadiusX; ++tempX)
			{
				double tempZ = this.radZ * Math.cos(Math.asin(tempX / this.radX));
				buf.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION);
				line.applyColour();
				
				buf.vertex(xPos + tempX, tmaxY - cameraPos.getY(), zPos + tempZ).next();
				buf.vertex(xPos + tempX, tmaxY - cameraPos.getY(), zPos - tempZ).next();
				buf.vertex(xPos + tempX, tminY - cameraPos.getY(), zPos - tempZ).next();
				buf.vertex(xPos + tempX, tminY - cameraPos.getY(), zPos + tempZ).next();
				buf.vertex(xPos + tempX, tmaxY - cameraPos.getY(), zPos + tempZ).next(); // 1st vertex repeated

				tessellator.draw();
			}
			
			for (double tempZ = negRadiusZ; tempZ <= posRadiusZ; ++tempZ)
			{
				double tempX = this.radX * Math.sin(Math.acos(tempZ / this.radZ));
				buf.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION);
				line.applyColour();
				
				buf.vertex(xPos + tempX, tmaxY - cameraPos.getY(), zPos + tempZ).next();
				buf.vertex(xPos - tempX, tmaxY - cameraPos.getY(), zPos + tempZ).next();
				buf.vertex(xPos - tempX, tminY - cameraPos.getY(), zPos + tempZ).next();
				buf.vertex(xPos + tempX, tminY - cameraPos.getY(), zPos + tempZ).next();
				buf.vertex(xPos + tempX, tmaxY - cameraPos.getY(), zPos + tempZ).next(); // 1st vertex repeated

				tessellator.draw();
			}
		}
	}
}
