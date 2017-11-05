package main;

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

	public Game(String title, int sizeX, int sizeY) {
		super(title, sizeX, sizeY);
		// TODO Auto-generated constructor stub
		width = sizeX;
		tank = new Tank(200, 200);
		bulletsList = new BulletsList(tank);
		background = new Background();
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

	@Override
	public void render(Graphics2D g, int arg1, int arg2) {
		// TODO Auto-generated method stub

		background.render(g);
		
		g.drawImage(tank.getTankImage(),tank.getTankTransform(),null);
		g.drawImage(tank.getTankTurretImage(),tank.getTurretTransform(),null);
		bulletsList.drawBullets(g);
		

		background.drawTank(tank.getX1(), tank.getY1(), g);
		
		
	}

	@Override
	public void update() {
		background.update(getMouseX(), getMouseY(),tank);
		
		if(isKeyDown(KeyEvent.VK_UP) || isKeyDown(KeyEvent.VK_W))tank.moveForward(background);
		if(isKeyDown(KeyEvent.VK_DOWN) || isKeyDown(KeyEvent.VK_S))tank.moveBackward(background);
		if(isKeyDown(KeyEvent.VK_RIGHT) || isKeyDown(KeyEvent.VK_D))tank.rotateTunkClocwise();
		if(isKeyDown(KeyEvent.VK_LEFT) || isKeyDown(KeyEvent.VK_A))tank.rotateTunkAntiClocwise();
		if(isKeyDown(KeyEvent.VK_SPACE) || isMouseButtonDown(GFMouseButton.Left))bulletsList.addBullet();
		
		bulletsList.moveBullets(width,height);
		
	}
	


}
