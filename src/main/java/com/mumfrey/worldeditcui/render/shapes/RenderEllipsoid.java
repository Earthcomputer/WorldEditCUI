package com.mumfrey.worldeditcui.render.shapes;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mumfrey.worldeditcui.render.LineStyle;
import com.mumfrey.worldeditcui.render.RenderStyle;
import com.mumfrey.worldeditcui.render.points.PointCube;
import com.mumfrey.worldeditcui.util.Vector3;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

/**
 * Draws an ellipsoid shape around a centre point.
 * 
 * @author yetanotherx
 * @author Adam Mummery-Smith
 */
public class RenderEllipsoid extends RenderRegion
{

	/**
	 * The number of intervals to draw in around the ellipsoid.
	 */
	private static final double STEPS = 40;
	protected final static double TAU = Math.PI * 2.0;
	
	protected PointCube centre;
	private final Vector3 radii;
	
	protected final double centreX, centreY, centreZ;
	
	public RenderEllipsoid(RenderStyle style, PointCube centre, Vector3 radii)
	{
		super(style);
		this.centre = centre;
		this.radii = radii;
		this.centreX = centre.getPoint().getX() + 0.5;
		this.centreY = centre.getPoint().getY() + 0.5;
		this.centreZ = centre.getPoint().getZ() + 0.5;
	}
	
	@Override
	public void render(Vector3 cameraPos)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translated(this.centreX - cameraPos.getX(), this.centreY - cameraPos.getY(), this.centreZ - cameraPos.getZ());
		
		for (LineStyle line : this.style.getLines())
		{
			if (line.prepare(this.style.getRenderType()))
			{
				this.drawXZPlane(line);
				this.drawYZPlane(line);
				this.drawXYPlane(line);
			}
		}

		GlStateManager.popMatrix();
	}
	
	protected void drawXZPlane(LineStyle line)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuffer();

		int yRad = (int)Math.floor(this.radii.getY());
		for (int yBlock = -yRad; yBlock < yRad; yBlock++)
		{
			buf.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION);
			line.applyColour();
			
			for (int i = 0; i <= STEPS + 1; i++)
			{
				double tempTheta = (i  % (STEPS + 1)) * TAU / STEPS; // overlap by one for LINE_STRIP
				double tempX = this.radii.getX() * Math.cos(tempTheta) * Math.cos(Math.asin(yBlock / this.radii.getY()));
				double tempZ = this.radii.getZ() * Math.sin(tempTheta) * Math.cos(Math.asin(yBlock / this.radii.getY()));
				
				buf.vertex(tempX, yBlock, tempZ).next();
			}
			tessellator.draw();
		}
		
		buf.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION);
		line.applyColour();
		
		for (int i = 0; i <= STEPS + 1; i++)
		{
			double tempTheta = (i  % (STEPS + 1)) * TAU / STEPS; // overlap by one for LINE_STRIP
			double tempX = this.radii.getX() * Math.cos(tempTheta);
			double tempZ = this.radii.getZ() * Math.sin(tempTheta);
			
			buf.vertex(tempX, 0.0, tempZ).next();
		}
		tessellator.draw();
	}
	
	protected void drawYZPlane(LineStyle line)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuffer();

		int xRad = (int)Math.floor(this.radii.getX());
		for (int xBlock = -xRad; xBlock < xRad; xBlock++)
		{
			buf.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION);
			line.applyColour();
			
			for (int i = 0; i <= STEPS + 1; i++)
			{
				double tempTheta = (i  % (STEPS + 1)) * TAU / STEPS; // overlap by one for LINE_STRIP
				double tempY = this.radii.getY() * Math.cos(tempTheta) * Math.sin(Math.acos(xBlock / this.radii.getX()));
				double tempZ = this.radii.getZ() * Math.sin(tempTheta) * Math.sin(Math.acos(xBlock / this.radii.getX()));
				
				buf.vertex(xBlock, tempY, tempZ).next();
			}
			tessellator.draw();
		}
		
		buf.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION);
		line.applyColour();
		
		for (int i = 0; i <= STEPS + 1; i++)
		{
			double tempTheta = (i  % (STEPS + 1)) * TAU / STEPS; // overlap by one for LINE_STRIP
			double tempY = this.radii.getY() * Math.cos(tempTheta);
			double tempZ = this.radii.getZ() * Math.sin(tempTheta);
			
			buf.vertex(0.0, tempY, tempZ).next();
		}
		tessellator.draw();
	}
	
	protected void drawXYPlane(LineStyle line)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuffer();

		int zRad = (int)Math.floor(this.radii.getZ());
		for (int zBlock = -zRad; zBlock < zRad; zBlock++)
		{
			buf.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION);
			line.applyColour();
			
			for (int i = 0; i <= STEPS + 1; i++)
			{
				double tempTheta = (i  % (STEPS + 1)) * TAU / STEPS; // overlap by one for LINE_STRIP
				double tempX = this.radii.getX() * Math.sin(tempTheta) * Math.sin(Math.acos(zBlock / this.radii.getZ()));
				double tempY = this.radii.getY() * Math.cos(tempTheta) * Math.sin(Math.acos(zBlock / this.radii.getZ()));
				
				buf.vertex(tempX, tempY, zBlock).next();
			}
			tessellator.draw();
		}
		
		buf.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION);
		line.applyColour();
		
		for (int i = 0; i <= STEPS + 1; i++)
		{
			double tempTheta = (i  % (STEPS + 1)) * TAU / STEPS; // overlap by one for LINE_STRIP
			double tempX = this.radii.getX() * Math.cos(tempTheta);
			double tempY = this.radii.getY() * Math.sin(tempTheta);
			
			buf.vertex(tempX, tempY, 0.0).next();
		}
		tessellator.draw();
	}
}
