package main;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
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
	
	public Patuljak() {
		frame = 0;
		image = Util.loadImage("character.png");
		imageW = image.getWidth() / TILE_W;
		imageH = image.getHeight() / TILE_H;

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
		if (speedY > 0)
			row = 2;
		if (speedY > 0)
			row = 3;
		
		int poljeX = (x) / 64;
		int poljeY = (y) / 64;


		if (poljeX > 0 && poljeY > 0 && poljeX < 16 && poljeY < 16
				&& Background.instance.getTileField(poljeX, poljeY) == 11) {
			fireB = true;
			System.out.println("VAATRA");
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g.drawImage(image, x - Background.instance.getCamX(), y - Background.instance.getCamY(),
				x + imageW - Background.instance.getCamX(), y + imageH - Background.instance.getCamY(), frame * imageW,
				row * imageH, (frame + 1) * imageW, (row + 1) * imageH, null);

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.00f));
		
		
		if (fireB)
			fire.render(g, camX, camY, x, y);

	}

	public void update() {

		if(dead){
			
			
			if(deadCounter >= 0){
			
				image = Util.loadImage("deadbody.png");
				System.out.println("Slika je : " + (image == null));
				deadCounter--;
				
				
				if(deadCounter % 3 == 0)
					alpha -= 0.01;
				System.out.println("Alpha: "+ alpha);
				alpha = Math.max(alpha, 0);
			}
			
			else {
				
				alpha = 1.00f;
				image = Util.loadImage("tileset/svgset6.png");
				
				
			}
			
			
		}
		
		
		else{

			System.out.println("X: " + (int) Tank.instance.getX()+ " " + x);
			System.out.println("Y: " + (int) Tank.instance.getY()+ " " + y);
			System.out.println();
		boolean coallision = MyUtil.checkCoallison(
				(int) Tank.instance.getX(), (int) Tank.instance.getY(),
				x, y,
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

			} else {

				speedY = 0;
				speedX = (r.nextInt(10) - 5) * 3;

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
