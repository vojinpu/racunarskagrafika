package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import rafgfxlib.Util;

public class Explosion {
	

	private BufferedImage image;
	
	private static final int TILE_W = 8;
	private static final int TILE_H = 6;
	
	
	public Explosion(){
		
		image = Util.loadImage("tileset/explosion.png");
		
		
	}
	
	public void draw(int x, int y, Graphics2D g){
		new Thread(new Runnable() {
			
			@Override
			public void run() {

				
				int height = image.getHeight() / TILE_H;
				int width = image.getWidth() / TILE_W;
				BufferedImage img = Util.loadImage("tileset/svgset0.png");
				
				System.out.println("Ekslozija");
				for(int i = 0; i < TILE_W; i++)
					for(int j = 0; j < TILE_H; j++){
						
						g.drawImage(image,
								x, y,
								x + 64, y + 64,
//								i * width, j * height,
//								(i + 1) * width, (j + 1) * height,
								i * width, j * height,
								(i + 1) * width, (j + 1) * height,
								
								null);
						
						
						try        
						{
						    Thread.sleep(5);
						} 
						catch(InterruptedException ex) 
						{
						    Thread.currentThread().interrupt();
						}

//						g.drawImage(img, x, y, 64, 64, null);
					}
				
				
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				}
		}).start();
		
		
		
	}
}
