package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import rafgfxlib.GameFrame;

public class Game extends GameFrame{
	
	int width;
	int height;
	Tank tank;
	BulletsList bulletsList;
	Background background;
	Explosions explosions;
	
	public static GameStatus gameStatus;
	
	enum GameStatus{
		INTRO,
		RUNNING,
		ANIMATION,
		END
	}

	public Game(String title, int sizeX, int sizeY) {
		super(title, sizeX, sizeY);
		// TODO Auto-generated constructor stub
		width = sizeX;
		tank = new Tank(200, 200);
		bulletsList = new BulletsList(tank);
		background = new Background();
		explosions = new Explosions();
		startThread();
		
	}

	@Override
	public void handleKeyDown(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleKeyUp(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseDown(int arg0, int arg1, GFMouseButton arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleMouseMove(int x, int y) {
		// TODO Auto-generated method stub
		tank.rotateTurret(x, y);
	}

	@Override
	public void handleMouseUp(int arg0, int arg1, GFMouseButton arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleWindowDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleWindowInit() {
		// TODO Auto-generated method stub
		
	}
	boolean a = true;
	@Override
	public void render(Graphics2D g, int arg1, int arg2) {
		// TODO Auto-generated method stub

		background.render(g);
		
		
		g.drawImage(tank.getTankImage(),tank.getTankTransform(),null);
		g.drawImage(tank.getTankTurretImage(),tank.getTurretTransform(),null);
		bulletsList.drawBullets(g);
		explosions.drawExplosions(g);
		
		
		for (int i = 0; i < background.getMapH(); ++i) {
			for (int j = 0; j < background.getMapW(); ++j) {
				if (background.getTileMap()[j][i] == 5) {
					int x2 = j * Background.getTileW() - background.getCamX();
					int y2 = i * Background.getTileH() - background.getCamY();
					g.fillRect(x2, y2, 10, 10);
				}
			}
		}
	}

	@Override
	public void update() {
		background.update(getMouseX(), getMouseY(),tank);
		
		if(isKeyDown(KeyEvent.VK_UP) || isKeyDown(KeyEvent.VK_W))tank.moveForward(background);
		if(isKeyDown(KeyEvent.VK_DOWN) || isKeyDown(KeyEvent.VK_S))tank.moveBackward(background);
		if(isKeyDown(KeyEvent.VK_RIGHT) || isKeyDown(KeyEvent.VK_D))tank.rotateTunkClocwise(background);
		if(isKeyDown(KeyEvent.VK_LEFT) || isKeyDown(KeyEvent.VK_A))tank.rotateTunkAntiClocwise(background);
		if(isKeyDown(KeyEvent.VK_SPACE) || isMouseButtonDown(GFMouseButton.Left))bulletsList.addBullet();
		
		bulletsList.moveBullets(width,height, background, explosions);
		
	}
	


}
