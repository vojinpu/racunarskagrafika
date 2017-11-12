package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import main.Game.GameStatus;

public class Rain {
	
	ArrayList<RainDrop> rainDrops;
	int width;
	int height;
	boolean isRaining;
	Random random;
	int counter;
	int addCounter = 1;
	
	public Rain(int width,int height) {
		this.width = width;
		this.height = height;
		isRaining = false;
		counter = 0;
		rainDrops = new ArrayList<>();
		random = new Random();
		for (int i = 0; i < 200; i++) {
			RainDrop rainDrop = new RainDrop();
			int size  = random.nextInt(20)+10;
			rainDrop.sizeX = size/2;
			rainDrop.sizeY = size;
			rainDrop.x = random.nextInt(width + 500)-500;
			rainDrop.y = random.nextInt(height)-height;
			rainDrops.add(rainDrop);
		}
		startRainMovement();
	}
	public void moveRain(int x,int y) {
		for(RainDrop rainDrop : rainDrops) {
			rainDrop.x+= x;
			rainDrop.y+= y;
		}
	}
	public void drawRain(Graphics2D g) {
		if(!isRaining)return;
		if(counter>510)counter=500;
		if(counter<0)counter=0;
		Color color = new Color(0, 0, 0, counter/3);
		g.setColor(color);
		g.fillRect(0, 0, width, height);
		color = new Color(255, 255, 255, counter/2);
		g.setColor(color);
		for(int i = 0;i < rainDrops.size();i++) {
		RainDrop drop = rainDrops.get(i);
		g.drawLine(drop.x,drop.y,drop.x+drop.sizeX,drop.y+drop.sizeY);
		}
	}
	public void startRainMovement() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					if(Game.gameStatus == GameStatus.END)return;
					if(Game.gameStatus == GameStatus.RUNNING)counter+=addCounter;
					if(counter >= 500)addCounter = -1;
						if(counter<=0) {
							isRaining = !isRaining;
							addCounter = 1;
						}
					for(int i = 0;i < rainDrops.size();i++) {
						RainDrop drop = rainDrops.get(i);
						drop.x++;
						drop.y+=2;
						if( drop.x> width || drop.y > height) {
							int size  = random.nextInt(20)+10;
							drop.sizeX = size/2;
							drop.sizeY = size;
							drop.x = random.nextInt(width + 500)-500;
							drop.y = 0;
						}
						}
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
	class RainDrop{
		int x,y;
		int sizeX,sizeY;
	}
}