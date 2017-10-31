package main;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

import rafgfxlib.Util;

public class MyUtil {
	public static void bilScale(WritableRaster src, float u, float v, int[] color)
	{
		int width = src.getWidth();
		int height = src.getHeight();
		
		u = Util.clamp(u - 0.5f, 0.0f, width - 1.0f);
		v = Util.clamp(v - 0.5f, 0.0f, height - 1.0f);
		
		int[] UL = new int[color.length];
		int[] UR = new int[color.length];
		int[] LL = new int[color.length];
		int[] LR = new int[color.length];

		int x0 = (int)u;
		int y0 = (int)v;
		int x1 = x0 + 1;
		int y1 = y0 + 1;
		
		if(x1 >= width) x1 = width - 1;
		if(y1 >= height) y1 = height - 1;
		
		float fX = u - x0;
		float fY = v - y0;
		
		src.getPixel(x0, y0, UL);
		src.getPixel(x1, y0, UR);
		src.getPixel(x0, y1, LL);
		src.getPixel(x1, y1, LR);
		
		int[] a = new int[color.length];
		int[] b = new int[color.length];
		
		if(color.length == 4)Util.lerpRGBAi(UL, UR, fX, a);
		else Util.lerpRGBi(UL, UR, fX, a);
		if(color.length == 4)Util.lerpRGBAi(LL, LR, fX, b);
		else Util.lerpRGBi(LL, LR, fX, b);
		if(color.length == 4)Util.lerpRGBAi(a, b, fY, color);
		else Util.lerpRGBi(a, b, fY, color);
	}
	
	public static BufferedImage scale(BufferedImage image,int scaleW,int scaleH){
		WritableRaster source = image.getRaster();
		WritableRaster target = Util.createRaster(scaleW, scaleH, source.getNumBands() == 4?true:false);
		int rgb[] = new int[source.getNumBands()];
		
		
		for(int y = 0; y < scaleH; y++)
		{
			float fy = (float)y / scaleH;
			
			for(int x = 0; x < scaleW; x++)
			{
				float fx = (float)x / scaleW;
				
				float srcX = fx * source.getWidth();
				float srcY = fy * source.getHeight();
				
				bilScale(source, srcX, srcY, rgb);
				
				target.setPixel(x, y, rgb);
				
			}
		}
		
		return Util.rasterToImage(target);
	}
	public static BufferedImage scaleSameProportion(BufferedImage image, float proportion ){
		WritableRaster source = image.getRaster();
		int scaleW = (int) (source.getWidth() * proportion);
		int scaleH = (int) (source.getHeight() * proportion);
		WritableRaster target = Util.createRaster(scaleW, scaleH, source.getNumBands() == 4?true:false);
		
		int rgb[] = new int[source.getNumBands()];
		
		
		for(int y = 0; y < scaleH; y++)
		{
			float fy = (float)y / scaleH;
			
			for(int x = 0; x < scaleW; x++)
			{
				float fx = (float)x / scaleW;
				
				float srcX = fx * source.getWidth();
				float srcY = fy * source.getHeight();
				
				bilScale(source, srcX, srcY, rgb);
				
				target.setPixel(x, y, rgb);
				
			}
		}
		
		return Util.rasterToImage(target);
	}
	public static BufferedImage horiozntalFlip(BufferedImage image ){
		WritableRaster source = image.getRaster();
		int scaleW = source.getWidth();
		int scaleH = source.getHeight();
		WritableRaster target = Util.createRaster(scaleW, scaleH, source.getNumBands() == 4?true:false);
		
		int rgb[] = new int[source.getNumBands()];
		
		
		for(int y = 0; y < scaleH; y++)
		{
			for(int x = 0; x < scaleW; x++)
			{
				int dX;
				if(x<source.getWidth()/2) {
					dX = source.getWidth()/2 + (source.getWidth()/2 - x);
				}else {
					dX = source.getWidth()/2 - (x - source.getWidth()/2);
				}
				source.getPixel(dX, y, rgb);
				target.setPixel(x, y, rgb);				
			}
		}
		
		return Util.rasterToImage(target);
	}
	public static BufferedImage addMask(BufferedImage image, BufferedImage mask,int xx, int yy) {
		WritableRaster source = mask.getRaster();
		int scaleW = source.getWidth();
		int scaleH = source.getHeight();
		WritableRaster target = image.getRaster();
		
		int rgba[] = new int[source.getNumBands()];
		int rgbTarget[] = new int[target.getNumBands()];
		
		
		for(int y = 0; y < scaleH; y++)
		{
			for(int x = 0; x < scaleW; x++)
			{
					source.getPixel(x, y, rgba);
					target.getPixel(x+xx, y+yy, rgbTarget);
					if(rgba[3]> 50) {
						rgbTarget[0] -= rgba[3];
						rgbTarget[1] -= rgba[3];
						rgbTarget[2] -= rgba[3];
						if(rgbTarget[0] < 0)rgbTarget[0] = 0;
						if(rgbTarget[1] < 0)rgbTarget[1] = 0;
						if(rgbTarget[2] < 0)rgbTarget[2] = 0;
						target.setPixel(x+xx, y+yy, rgbTarget);
					}
			}
		}
		
		return Util.rasterToImage(target);
	}
}
