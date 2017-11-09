package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Random;

import javax.annotation.Generated;
import javax.naming.ldap.StartTlsRequest;

import rafgfxlib.Util;

public class Intro {
	Game game;
	BufferedImage image;
	int k;
	Star[] stars;
	Random r;
	public Intro(Game game){
		this.game = game;
		r = new Random();
		initStars();
		generateRaster();
		Background.instance.setCamY(-3000);
		startIntroAnimation();	
	}
	
	private class Star {
		int x,y;
		int size;
		public Star(int x, int y, int size) {
			super();
			this.x = x;
			this.y = y;
			this.size = size;
		}
		
	}
	public void initStars(){
		stars = new Star[100];
		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Star(r.nextInt(game.width),r.nextInt(game.height),r.nextInt(15)*2+1);
		}
	}
	
	public void moveStars(int movement){
		for(Star star : stars){
			
			star.y -= movement;
			if(star.y + star.size/2 < 0){
				if(k > 500)star = null;
				else {
				star.size = r.nextInt(15)*2+1;
				star.x = r.nextInt(game.width);
				star.y = game.height-1 + star.size/2;
				}
			}
		}
	}
	
	public void generateRaster(){
		WritableRaster raster = Util.createRaster(game.width, game.height, false);
		int rgb[] = new int[3];
		
		
		for(float y = 0; y < game.height; y++)
		{	
			for(int x = 0; x < game.width; x++)
			{
				rgb[0] = (int) ((y * (150.0f / game.height) / 1000)*k) + k/30;
				rgb[1] = (int) ((y * (150.0f / game.height) / 1000)*k) + k/30;
				rgb[2] = (int) ((y * (155.0f / game.height) / 1000)*k) + k/10;
				raster.setPixel(x, (int)y, rgb);
			}
		}
		
		for(Star star : stars){
			int x1 = star.x;
			int y1 = star.y;
			if(x1 < 0)x1 = 0;
			if(x1 > game.width-1)x1 = game.width-1;
			if(y1 < 0)y1=0;
			if(y1 > game.height-1)y1 = game.height-1;
			int[] color = new int[3];
			color = raster.getPixel(x1, y1, color);
			color[0] += (255f - color[0]) * (1000-k)/1000f;
			color[1] += (255f - color[1]) * (1000-k)/1000f;
			color[2] += (255f - color[2]) * (1000-k)/1000f;
			for (int i = -star.size/2; i < star.size/2; i++) {
				if(star.x + i >= 0 && star.x + i < game.width && star.y >= 0 && star.y < game.height){
					raster.setPixel(star.x + i, star.y,  color);
				}
				if(star.y + i >= 0 && star.y + i < game.height && star.x >= 0 && star.x < game.width){
					raster.setPixel(star.x, star.y + i,  color);
				}
				if(star.x + i >= 0 && star.x + i < game.width && star.y + i >= 0 && star.y + i < game.height && i > -star.size/2+4 && i < star.size/2-4){
					raster.setPixel(star.x + i, star.y + i,  color);
				}
				if(star.x + i >= 0 && star.x + i < game.width && star.y - i >= 0 && star.y - i < game.height && i > -star.size/2+4 && i < star.size/2-4){
					raster.setPixel(star.x + i, star.y - i,  color);
				}
			}
		}
		
		image = Util.rasterToImage(raster);
	}
	public BufferedImage getImage(){
		return image;
	}
	public void startIntroAnimation(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(Game.gameStatus == Game.GameStatus.INTRO ){
					if(k < 1000)k+=5;
					moveStars(10);
					Background.instance.setCamY(Background.instance.getCamY()+10);
					Tank.instance.move(0, 1);
					generateRaster();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
