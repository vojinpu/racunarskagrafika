package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import Plamen.Fire;
import rafgfxlib.Util;

public class Patuljak {

	private BufferedImage image;
	private static final int TILE_W = 8;
	private static final int TILE_H = 4;

	private int imageW, imageH;

	private int frame;
	private int speedX, speedY;
	private int x, y;
	private Random r;
	private int moves;

	private boolean dead;

	private Fire fire;
	private boolean fireB;
	
	private int deadCounter = 300;
	private float alpha = 1.00f;
	
	private boolean deadPhoto = true;
	
	public Patuljak() {
		frame = 0;
		image = Util.loadImage("character.png");
		imageW = image.getWidth() / TILE_W;
		imageH = image.getHeight() / TILE_H;
		
		speedX = -10;

		moves = 200;

		x = 100;
		y = 100;

		r = new Random();

		fire = new Fire();
		fireB = false;
		dead = false;
	}

	public void draw(Graphics2D g, int camX, int camY) {

		int row = 0;
		

		if (speedX < 0)
			row = 0;
		if (speedX > 0)
			row = 1;
		if (speedY < 0)
			row = 2;
		if (speedY > 0)
			row = 3;
		
		int poljeX = (x + imageW/2) / 64;
		int poljeY = (y + imageH/2) / 64;


		if (poljeX > 0 && poljeY > 0 && poljeX < 16 && poljeY < 16
				&& Background.instance.getTileField(poljeX, poljeY) == 11) {
			fireB = true;
		}
		
		if(deadCounter < 300){
			
			if(deadCounter < 0){
			
				Background.instance.setTileMap(poljeX, poljeY, 6);
				
			}
			else{
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			
			g.drawImage(image, x - Background.instance.getCamX(), y - Background.instance.getCamY(),
					image.getWidth(), image.getHeight(), null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.00f));
			}
		}
		else
		g.drawImage(image, x - Background.instance.getCamX(), y - Background.instance.getCamY(),
				x + imageW - Background.instance.getCamX(), y + imageH - Background.instance.getCamY(), frame * imageW,
				row * imageH, (frame + 1) * imageW, (row + 1) * imageH, null);

		
		
		if (fireB)
			fire.render(g, camX, camY, x, y);

	}

	public void update() {

		if(dead){
			
			
			if(deadCounter >= 0){
			
				image = Util.loadImage("deadbody.png");
				deadCounter--;
				
				if(deadCounter % 3 == 0)
					alpha -= 0.01;
				
				alpha = Math.max(alpha, 0);
			}
			
			else {
				fireB = false;
				alpha = 1.00f;
				image = Util.loadImage("tileset/svgset6.png");
				
				
			}
			
			
		}
		
		
		else{
		boolean coallision = MyUtil.checkCoallison(
				x - Background.instance.getCamX(), y - Background.instance.getCamY(),
				Tank.instance.getTankImage().getWidth(), Tank.instance.getTankImage().getHeight(),
				imageW, imageH, Tank.instance.getTankTransform());
		
		if(coallision){
			System.out.println("UMRO JE");
			dead = true;
		}
		
		
		
		frame++;
		if (frame >= TILE_W)
			frame = 0;

		moves--;
		if (moves == 0) {
			moves = r.nextInt(500) + 200;
			if (r.nextInt() % 2 == 0) {

				speedX = 0;
				speedY = (r.nextInt(10) - 5) * 3;
				if(speedY == 0)
					speedY = 5;

			} else {

				speedY = 0;
				speedX = (r.nextInt(10) - 5) * 3;
				if(speedX == 0)
					speedX = 5;

			}

		}

		if (x + speedX < 0 || x + speedX > (Background.instance.getMapW() - 1) * 64)
			speedX *= -1;

		if (y + speedY < 0 || y + speedY > (Background.instance.getMapH() - 1) * 64)
			speedY *= -1;

		x += speedX;
		y += speedY;

		if (fireB)
			fire.update();
		}
	}

}
