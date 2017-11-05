package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import rafgfxlib.GameFrame;
import rafgfxlib.Util;
import rafgfxlib.GameFrame.GFMouseButton;

public class Background {
	private static final long serialVersionUID = 7636268720113395780L;

	private static final int TILE_W = 64;
	private static final int TILE_H = 64;

	private int mapW = 15;
	private int mapH = 15;

	private int camX = 0;
	private int camY = 0;

	private int selX = 0;
	private int selY = 0;

	static int windowHeight = 640;
	static int windowWidth = 640;

	private class Tile {
		public BufferedImage image = null;
		public int offsetX = 0;
		public int offsetY = 0;
		@SuppressWarnings("unused")
		public int tileID = 0;

		public Tile(String fileName, int ID) {
			image = Util.loadImage(fileName);
			tileID = ID;
			if (image != null) {
				offsetX = 0;
				offsetY = -(image.getHeight() - TILE_H);
			} else {
				System.out.println("Fail at \"" + fileName + "\"");
			}
		}
	}

	private Tile[] tileset = new Tile[32];
	private int[][] tileMap = new int[mapW][mapH];

	public Background() {

		for (int i = 0; i <= 16; ++i) {
			tileset[i] = new Tile("tileset/svgset" + i + ".png", i);
		}

		
		Random rnd = new Random();
		for (int y = 0; y < mapH; ++y) {
			for (int x = 0; x < mapW; ++x) {
				tileMap[x][y] = Math.abs(rnd.nextInt()) % 3;
//				tileMap[x][y] = 2;
			}
		}


		int grobljeX = Math.abs(rnd.nextInt()) % mapW;
		int grobljeY = Math.abs(rnd.nextInt()) % mapH;

		for(int i = -1; i < 2; i++)
			for(int j = -1; j < 2; j++)
				if(grobljeX + i >= 0 && grobljeX + i < mapW
						&& grobljeY + j >= 0 && grobljeY + j < mapH)
					tileMap[grobljeX + i][grobljeY + j] = 6;
		
		for (int i = 0; i < mapW * mapH / 7; ++i) {
			int x = Math.abs(rnd.nextInt()) % mapW;
			int y = Math.abs(rnd.nextInt()) % mapH;
			int tree = Math.abs(rnd.nextInt()) % 3;
			tileMap[x][y] = 3 + tree;
			
//			if(3 + tree == 4)
//				System.out.println("JESTEE");
		}

		
		// startThread();
	}

	public void render(Graphics2D g) {

		int x0 = camX / TILE_W;
		int x1 = x0 + (windowWidth / TILE_W) + 1;
		int y0 = camY / TILE_H;
		int y1 = y0 + (windowHeight / TILE_H) + 1;

		if (x0 < 0)
			x0 = 0;
		if (y0 < 0)
			y0 = 0;
		if (x1 < 0)
			x1 = 0;
		if (y1 < 0)
			y1 = 0;

		if (x0 >= mapW)
			x0 = mapW - 1;
		if (y0 >= mapH)
			y0 = mapH - 1;
		if (x1 >= mapW)
			x1 = mapW - 1;
		if (y1 >= mapH)
			y1 = mapH - 1;

		for (int y = y0; y <= y1; ++y) {
			for (int x = x0; x <= x1; ++x) {
				g.drawImage(tileset[tileMap[x][y]].image, x * TILE_W + tileset[tileMap[x][y]].offsetX - camX,
						y * TILE_H + tileset[tileMap[x][y]].offsetY - camY, null);
			}
		}

		g.setColor(Color.yellow);
		g.drawRect(selX * TILE_W - camX, selY * TILE_H - camY, TILE_W, TILE_H);

	}
	
	public void drawTank(int x, int y, Graphics2D g){
		
//		System.out.println("Camx; x; Camx - x " + camX +" " + x + " " + (camX - x));
		int poljeX = (x + camX) / TILE_W;
		int poljeY = (y + camY) / TILE_H;
		
		if(tileMap[poljeX][poljeY] == 3)
			tileMap[poljeX][poljeY] = 8;
		
		
		g.setColor(Color.red);
		g.drawRect(x, y, TILE_W, TILE_H);
		
//		return (x > selX * TILE_W - camX && x < (selX + 1) * TILE_W - camX &&
//				y > selY * TILE_H - camY && y < (selY + 1) * TILE_H - camY);
		
	}

	public void update(int mouseX, int mouseY, Tank tank) {
		int camXMovement = camX;
		int camYMovement = camY;
		// System.out.println("getMouseX()" + " " + mouseX);
		// System.out.println("getMouseY()" + " " + mouseY);
		if (mouseX < windowWidth / 10)
			camX -= 10;
		if (mouseX > windowWidth / 10 * 9)
			camX += 10;
		if (mouseY < windowHeight / 10)
			camY -= 10;
		if (mouseY > windowHeight / 10 * 9)
			camY += 10;

		// System.out.println(camX + " " + (mapW - 7) * TILE_W);

		if (camX < -2 * TILE_W)
			camX = -2 * TILE_W;

		if (camX > (mapW - 7) * TILE_W)
			camX = (mapW - 7) * TILE_W;
		if (camY < -2 * TILE_H)
			camY = -2 * TILE_H;
		if (camY > (mapH - 7) * TILE_H)
			camY = (mapH - 7) * TILE_H;

		selX = (mouseX + camX) / TILE_W;
		selY = (mouseY + camY) / TILE_H;

		if (selX < 0)
			selX = 0;
		if (selY < 0)
			selY = 0;
		if (selX >= mapW)
			selX = mapW - 1;
		if (selY >= mapH)
			selY = mapH - 1;

		camXMovement -= camX;
		camYMovement -= camY;
		if (camXMovement != 0 || camYMovement != 0)
			tank.move(camXMovement, camYMovement);

	}

	public boolean isTankAbleToMove(int x, int y) {
		
		
		boolean bound = (x + camX > 0 && y + camY > 0
				&& x + camX < TILE_W * mapW && y + camY < TILE_H * mapH);
		

		int poljeX = (x + camX) / TILE_W;
		int poljeY = (y + camY) / TILE_H;
		
		boolean rock = (tileMap[poljeX][poljeY] != 4 && tileMap[poljeX][poljeY] != 6);
		
		
		
		return bound && rock;
	}

}
