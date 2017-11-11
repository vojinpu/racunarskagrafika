package Plamen;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import rafgfxlib.GameFrame;

public class Fire {

	private Random r;
	private int iskreNum = 700;
	private Iskra iskre [] = new Iskra[iskreNum];
	
	private int x,y;
	
	public Fire(){

		r = new Random();
		createIskre();
	}
	
	public Fire(int x, int y) {
		this.x = x;
		this.y = y;
		
		r = new Random();
		createIskre();
	}
	
	
	private void createIskre() {
		
		for(int i = 0; i < iskreNum; i++) {
			iskre[i] = new Iskra();
		}
		
	}

	public void render(Graphics2D g, int camX, int camY) {


		g.setColor(new Color(156 + r.nextInt(60), 42  + r.nextInt(10), 15  + r.nextInt(10)));

		for(int i = 0; i < iskreNum; i++) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, iskre[i].getAlpha()));
			g.drawRect(x - camX + iskre[i].getX(), y - camY + 64 - iskre[i].getY(), 1, 1);
		}
		
		//restore Graphics alpha to original state
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		
	}
	
	public void render(Graphics2D g, int camX, int camY, int x, int y) {


		g.setColor(new Color(156 + r.nextInt(60), 42  + r.nextInt(10), 15  + r.nextInt(10)));

		for(int i = 0; i < iskreNum; i++) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, iskre[i].getAlpha()));
			g.drawRect(x - camX + iskre[i].getX(), y - camY + 64 - iskre[i].getY(), 1, 1);
		}
		
		//restore Graphics alpha to original state
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		
	}

	public void update() {

		for(int i = 0; i < iskreNum; i++) {
			iskre[i].update();
		}
	}

}
