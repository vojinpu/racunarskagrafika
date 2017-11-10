package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import Plamen.Fire;
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

	static int windowHeight = 800;
	static int windowWidth = 1000;
	
	private int counterTarget = 0;

	private Fire fire;
	
	public static Background instance;

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

	public Background(Tank tank) {
		
		instance = this;

		fire = new Fire();

		for (int i = 0; i <= 16; ++i) {
			tileset[i] = new Tile("tileset/svgset" + i + ".png", i);
		}

		Random rnd = new Random();
		for (int y = 0; y < mapH; ++y) {
			for (int x = 0; x < mapW; ++x) {
				tileMap[x][y] = Math.abs(rnd.nextInt()) % 3;
				// tileMap[x][y] = 2;
			}
		}

		int grobljeX = Math.abs(rnd.nextInt()) % mapW;
		int grobljeY = Math.abs(rnd.nextInt()) % mapH;

		for (int i = -1; i < 2; i++)
			for (int j = -1; j < 2; j++)
				if (grobljeX + i >= 0 && grobljeX + i < mapW && grobljeY + j >= 0 && grobljeY + j < mapH)
					tileMap[grobljeX + i][grobljeY + j] = 6;

	
		for (int i = 0; i < mapW * mapH / 7; ++i) {

			int x = Math.abs(rnd.nextInt()) % mapW;
			int y = Math.abs(rnd.nextInt()) % mapH;
			int tree = Math.abs(rnd.nextInt()) % 4;
			tileMap[x][y] = 3 + tree;
			if(tileMap[x][y] ==  6){
				tileMap[x][y] = 10;
				counterTarget++;
			}

		}

		tileMap[5][5] = 9;
		int startX = (tank.getStartX() + camX) / TILE_W;
		int startY = (tank.getStartY() + camY) / TILE_H;
		for (int i = -1; i <=  2; i++)
			for (int j = -1; j <= 2; j++)
				if (startX + i >= 0 && startX + i < mapW && startY + j >= 0 && startY + j < mapH){
					if(tileMap[startX + i][startY + j] ==  6)
						counterTarget--;
					tileMap[startX + i][startY + j] = 5;
				}
		
		
		
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

		fire.render(g, 5 * 64 - camX, 5 * 64 - camY);
		
		if(drawCoallision)g.drawRect(cX - camX, cY - camY, TILE_H, TILE_W);
		
		   AffineTransform transform = Tank.instance.getTankTransform();
		   Point2D point = new Point2D.Double();
	       transform.transform(new Point2D.Double(0,0), point);
	       g.drawRect((int)point.getX(),(int) point.getY(), 10, 10);
	       transform.transform(new Point2D.Double(0,Tank.instance.getTankImage().getHeight()), point);
	       g.drawRect((int)point.getX(),(int) point.getY(), 10, 10);
	       transform.transform(new Point2D.Double(Tank.instance.getTankImage().getWidth(),0), point);
	       g.drawRect((int)point.getX(),(int) point.getY(), 10, 10);
	       transform.transform(new Point2D.Double(Tank.instance.getTankImage().getWidth(),Tank.instance.getTankImage().getHeight()), point);
	       g.drawRect((int)point.getX(),(int) point.getY(), 10, 10);
	       transform.transform(new Point2D.Double(0,Tank.instance.getTankImage().getHeight()/2), point);
	       g.drawRect((int)point.getX(),(int) point.getY(), 10, 10);
	       transform.transform(new Point2D.Double(Tank.instance.getTankImage().getWidth(),Tank.instance.getTankImage().getHeight()/2), point);
	       g.drawRect((int)point.getX(),(int) point.getY(), 10, 10);
	}

	public void drawTank(int x, int y, Graphics2D g) {

		int poljeX = (x + camX) / TILE_W;
		int poljeY = (y + camY) / TILE_H;

		if (tileMap[poljeX][poljeY] == 3)
			tileMap[poljeX][poljeY] = 8;

		g.setColor(Color.red);
		g.drawRect(x, y, TILE_W, TILE_H);


	}

	public void update(int mouseX, int mouseY, Tank tank) {
		int camXMovement = camX;
		int camYMovement = camY;

		if (mouseX < windowWidth / 10)
			camX -= 10;
		if (mouseX > windowWidth / 10 * 9)
			camX += 10;
		if (mouseY < windowHeight / 10)
			camY -= 10;
		if (mouseY > windowHeight / 10 * 9)
			camY += 10;

		if (camX < -2 * TILE_W)
			camX = -2 * TILE_W;

		if (camX > (mapW - windowWidth / TILE_W + 2) * TILE_W)
			camX = (mapW - windowWidth / TILE_W + 2) * TILE_W;
		if (camY < -2 * TILE_H)
			camY = -2 * TILE_H;
		if (camY > (mapH  - windowHeight / TILE_H + 2) * TILE_H)
			camY = (mapH  - windowHeight / TILE_H + 2) * TILE_H;

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

		fire.update();

	}

	int cX, cY;
	boolean drawCoallision;
	public boolean check(int x1, int y1) {
		for (int i = 0; i < mapH; ++i) {
			for (int j = 0; j < mapW; ++j) {
				if (tileMap[j][i] == 4 || tileMap[j][i] == 6 || tileMap[j][i] == 9|| tileMap[j][i] == 3) {
					int x2 = j * TILE_W - camX;
					int y2 = i * TILE_H - camY;
					boolean coallision = MyUtil.checkCoallison(x1, y1, x2, y2, Tank.instance.getTankImage().getWidth(),
							Tank.instance.getTankImage().getHeight(), TILE_W, TILE_H, Tank.instance.getTankTransform());
					if (coallision) {
						cX = x2 + camX;
						cY = y2 + camY;
						if(tileMap[j][i] == 9){
							Tank.instance.startWaveAnimation();
							return false;
						}
						
						if(tileMap[j][i] == 3){
							tileMap[j][i] = 8;
							return false;
						}
						
						
						drawCoallision = true;
						return false;
					}
				}
			}
		}
		drawCoallision = false;
		return true;

	}
	public boolean isTankAbleToMove(int x, int y) {
		boolean bound = MyUtil.checkBound(-camX, -camY, TILE_W * mapW, TILE_H * mapH, Tank.instance.getTankTransform());
		return check(x, y) && bound;
	}

	public static int getTileW() {
		return TILE_W;
	}

	public static int getTileH() {
		return TILE_H;
	}

	public int getMapW() {
		return mapW;
	}

	public int getMapH() {
		return mapH;
	}

	public int getCamX() {
		return camX;
	}

	public int getCamY() {
		return camY;
	}

	public int[][] getTileMap() {
		return tileMap;
	}
	
	public void setTileMapDestroyed(int x, int y) {
		tileMap[x][y] = 11;
	}

	public void setCamX(int camX) {
		this.camX = camX;
	}

	public void setCamY(int camY) {
		this.camY = camY;
	}
	
	public void decriseTargets(){
		--counterTarget;
	}
	
	

}
