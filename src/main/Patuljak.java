package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import Plamen.Fire;
import rafgfxlib.Util;

public class Patuljak {
	
	private BufferedImage image;
	private static final int TILE_W = 10;
	private static final int TILE_H = 4;
	
	private int imageW, imageH;
	
	private int frame;
	private int speedX, speedY;
	private int x,y;
	private Random r;
	private int moves;
	
	
	private Fire fire;
	private boolean fireB;
	public Patuljak(){
		frame = 0;
		image = Util.loadImage("character.png");
		imageW = image.getWidth() / 10;
		imageH = image.getHeight() / 4;
		
		moves = 200;
		
		x = 100;
		y = 100;
		
		r = new Random();
		
		fire = new Fire();
		fireB = false;
	}
	
	
	public void draw(Graphics2D g, int camX, int camY){

		int row = 0;
		if(speedY > 0)
			row = 0;
		if(speedX < 0)
			row = 1;
		if(speedY < 0)
			row = 2;
		if(speedX > 0)
			row = 3;
		
		int poljeX = (x ) / 64;
		int poljeY = (y ) / 64;

		System.out.println("poljeX " + poljeX + " " + x + " " + camX);
		System.out.println("poljeY " + poljeY + " " + y + " " + camY);
		System.out.println();
		
		if(poljeX > 0 && poljeY > 0 && poljeX < 16 && poljeY < 16 && Background.instance.getTileField(poljeX, poljeY) == 11){
			fireB = true;
			System.out.println("VAATRA");
		}
		
		g.drawImage(image, x - Background.instance.getCamX(), y - Background.instance.getCamY(), 
				x + 64 - Background.instance.getCamX(), y + 64 - Background.instance.getCamY(), 
				frame*imageW, row * imageH, (frame+1)*imageW, (row + 1) * imageH, null);
		
		if(fireB)
			fire.render(g, camX, camY, x, y);
			
	}
	
	public void update(){
		
		frame++;
		if(frame >= TILE_W)
			frame = 0;
		
		
		moves--;
		if(moves == 0){
			moves = r.nextInt(500) + 200;
			if(r.nextInt() % 2 == 0){
				
				speedX = 0;
				speedY = (r.nextInt(10) - 5) * 3;
				
			}
			else{

				speedY = 0;
				speedX = (r.nextInt(10) - 5) * 3;
				
			}
			
			
		}
		

		if(x + speedX < 0 || x + speedX > (Background.instance.getMapW() - 1) * 64)
			speedX *= -1;
		
		if(y + speedY < 0 || y + speedY > (Background.instance.getMapH() - 1)* 64)
			speedY *= -1;
		
		x += speedX;
		y += speedY;
		
		if(fireB)
		fire.update();
		
		
		
	}
	


}
