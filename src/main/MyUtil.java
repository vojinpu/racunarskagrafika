package main;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.security.cert.CertPathChecker;

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
	public static BufferedImage addWaves(BufferedImage image, int k) {
		WritableRaster source = image.getRaster();
		WritableRaster target = Util.createRaster(image.getWidth()  , image.getHeight(), true);
		
		int rgb[] = new int[4];
		
		// Snaga distorzije
		float power = 15.0f;
		// Velicina talasa
		float size = 0.10f;
		
		for(int y = 0; y < image.getHeight(); y++)
		{			
			for(int x = 0; x < image.getWidth(); x++)
			{
				// Funkcijama sin() i cos() deformisemo koordinate
				float srcX = (float)(x + Math.sin((y+k) * size) * power);
				float srcY = (float)(y + Math.cos((x+k) * size) * power);
				
				// Koristimo deformisane koordinate za citanje bilinearnog uzorka
				bilSampleA(source, srcX, srcY, rgb);
				if(rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0) {
					if(x < target.getWidth()/10 || x > target.getWidth()/10*9 || y < target.getHeight()/10 || y > target.getHeight()/10*9) {
						rgb[3] = 0;	
					}
				}
				target.setPixel(x, y, rgb);
			}
		}
		
		return Util.rasterToImage(target);
	}
	public static void bilSampleA(WritableRaster src, float u, float v, int[] color)
	{
		float[] a = new float[4];
		float[] b = new float[4];
		
		int[] UL = new int[4];
		int[] UR = new int[4];
		int[] LL = new int[4];
		int[] LR = new int[4];

		int x0 = (int)u;
		int y0 = (int)v;
		int x1 = x0 + 1;
		int y1 = y0 + 1;
		
		u -= x0;
		v -= y0;
		
		if(x0 < 0) x0 = 0;
		if(y0 < 0) y0 = 0;
		if(x1 < 0) x1 = 0;
		if(y1 < 0) y1 = 0;
		
		if(x0 >= src.getWidth()) x0 = src.getWidth() - 1;
		if(y0 >= src.getHeight()) y0 = src.getHeight() - 1;
		if(x1 >= src.getWidth()) x1 = src.getWidth() - 1;
		if(y1 >= src.getHeight()) y1 = src.getHeight() - 1;
		
		src.getPixel(x0, y0, UL);
		src.getPixel(x1, y0, UR);
		src.getPixel(x0, y1, LL);
		src.getPixel(x1, y1, LR);
		
		Util.lerpRGBAif(UL, UR, u, a);
		Util.lerpRGBAif(LL, LR, u, b);
		
		color[0] = (int)(Util.lerpF(a[0], b[0], v));
		color[1] = (int)(Util.lerpF(a[1], b[1], v));
		color[2] = (int)(Util.lerpF(a[2], b[2], v));
		color[3] = (int)(Util.lerpF(a[3], b[3], v));
		
		Util.clampRGBA(color);
	}
	public static BufferedImage rotateImage(BufferedImage image, double rotation){
		WritableRaster source = image.getRaster();
		int size = (int) Math.sqrt(source.getWidth()*source.getWidth()+source.getHeight()*source.getHeight());
		WritableRaster target = Util.createRaster(size, size, source.getNumBands() == 4?true:false);
		
        int width  = source.getWidth();
        int height = source.getHeight();

        double angle = Math.toRadians(50);
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double x0 =          		(size  - 1);     // point to rotate about
        double y0 = 0.2 * (size - 1);     // center of image


        int max = 0;
        
        // rotation
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                double a = x - x0;
                double b = y - y0;
                int xx = (int) (a * cos - b * sin + x0);
                int yy = (int) (a * sin + b * cos + y0);
                if(xx > max){
                	max = xx;
                	System.out.println(a+","+b+"," + xx);
                }

                // plot pixel (x, y) the same color as (xx, yy) if it's in bounds
                if (xx >= 0 && xx < width && yy >= 0 && yy < height) {
                	int[] rgba = new int[4];
                	source.getPixel(xx, yy, rgba);
                    target.setPixel(x , y , rgba);
                }
            }
        }

        return Util.rasterToImage(target);
	}
	
	public static boolean checkCoallison(int x2, int y2, int width1, int height1, int width2, int height2, AffineTransform transform){
	       Point2D point = new Point2D.Double();
	       transform.transform(new Point2D.Double(0,0), point);
	       if (point.getX() >= x2 && point.getX() <= x2 + width2 && point.getY() >= y2 && point.getY() <= y2 + height2) return true;
	       transform.transform(new Point2D.Double(0,height1), point);
	       if (point.getX() >= x2 && point.getX() <= x2 + width2 && point.getY() >= y2 && point.getY() <= y2 + height2) return true;
	       transform.transform(new Point2D.Double(width1,0), point);
	       if (point.getX() >= x2 && point.getX() <= x2 + width2 && point.getY() >= y2 && point.getY() <= y2 + height2) return true;
	       transform.transform(new Point2D.Double(width1,height1), point);
	       if (point.getX() >= x2 && point.getX() <= x2 + width2 && point.getY() >= y2 && point.getY() <= y2 + height2) return true;
	       transform.transform(new Point2D.Double(0,height2/2), point);
	       if (point.getX() >= x2 && point.getX() <= x2 + width2 && point.getY() >= y2 && point.getY() <= y2 + height2) return true;
	       transform.transform(new Point2D.Double(width1,height1/2), point);
	       if (point.getX() >= x2 && point.getX() <= x2 + width2 && point.getY() >= y2 && point.getY() <= y2 + height2) return true;
	       transform.transform(new Point2D.Double(width1/2,0), point);
	       if (point.getX() >= x2 && point.getX() <= x2 + width2 && point.getY() >= y2 && point.getY() <= y2 + height2) return true;
	       transform.transform(new Point2D.Double(width1/2,height1), point);
	       if (point.getX() >= x2 && point.getX() <= x2 + width2 && point.getY() >= y2 && point.getY() <= y2 + height2) return true;
	       return false;
	}
	
	public static boolean checkBound(int x, int y,int width,int height, AffineTransform transform){
	       Point2D point = new Point2D.Double();
	       transform.transform(new Point2D.Double(0,0), point);
	       if (point.getX() < x || point.getX() > x + width || point.getY() < y || point.getY() > y + height) return false;
	       transform.transform(new Point2D.Double(0,Tank.instance.getTankImage().getHeight()), point);
	       if (point.getX() < x || point.getX() > x + width || point.getY() < y || point.getY() > y + height) return false;
	       transform.transform(new Point2D.Double(Tank.instance.getTankImage().getWidth(),0), point);
	       if (point.getX() < x || point.getX() > x + width || point.getY() < y || point.getY() > y + height) return false;
	       transform.transform(new Point2D.Double(Tank.instance.getTankImage().getWidth(),Tank.instance.getTankImage().getHeight()), point);
	       if (point.getX() < x || point.getX() > x + width || point.getY() < y || point.getY() > y + height) return false;
	       transform.transform(new Point2D.Double(0,Tank.instance.getTankImage().getHeight()/2), point);
	       if (point.getX() < x || point.getX() > x + width || point.getY() < y || point.getY() > y + height) return false;
	       transform.transform(new Point2D.Double(Tank.instance.getTankImage().getWidth(),Tank.instance.getTankImage().getHeight()/2), point);
	       if (point.getX() < x || point.getX() > x + width || point.getY() < y || point.getY() > y + height) return false;
	       return true;
	}
}
