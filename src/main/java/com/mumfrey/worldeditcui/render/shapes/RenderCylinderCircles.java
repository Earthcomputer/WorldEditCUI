package com.mumfrey.worldeditcui.render.shapes;

import com.mumfrey.worldeditcui.render.LineStyle;
import com.mumfrey.worldeditcui.render.RenderStyle;
import com.mumfrey.worldeditcui.render.points.PointCube;
import com.mumfrey.worldeditcui.util.Vector3;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

/**
 * Draws the circles around a cylindrical region
 * 
 * @author yetanotherx
 * @author Adam Mummery-Smith
 */
public class RenderCylinderCircles extends RenderRegion
{
	private final double radX;
	private final double radZ;
	private final int minY;
	private final int maxY;
	private final double centreX;
	private final double centreZ;
	
	public RenderCylinderCircles(RenderStyle style, PointCube centre, double radX, double radZ, int minY, int maxY)
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
			
			double twoPi = Math.PI * 2;
			for (int yBlock = this.minY + 1; yBlock <= this.maxY; yBlock++)
			{
				buf.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION);
				line.applyColour();
				
				for (int i = 0; i <= 75; i++)
				{
					double tempTheta = i * twoPi / 75;
					double tempX = this.radX * Math.cos(tempTheta);
					double tempZ = this.radZ * Math.sin(tempTheta);
					
					buf.vertex(xPos + tempX, yBlock - cameraPos.getY(), zPos + tempZ).next();
				}

				// And back to initial vertex (because we have to use LINE_STRIP rather than LINE_LOOP)
				buf.vertex(xPos + this.radX, yBlock - cameraPos.getY(), zPos).next();

				tessellator.draw();
			}
		}
	}
}
