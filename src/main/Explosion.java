package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import rafgfxlib.Util;

public class Explosion {

	private BufferedImage image;

	private static final int TILE_W = 9;
	private static final int TILE_H = 9;

	int x, y;

	int x1, y1;
	int x2, y2;

	Explosions explosions;
	private Explosion instance;

	public Explosion(Explosions explosions, int x, int y) {
		instance = this;
		image = Util.loadImage("tileset/explosion.png");
		this.x = x + Background.instance.getCamX();
		this.y = y + Background.instance.getCamY();
		this.explosions = explosions;
		startExplosionAnimation();
	}
	public void draw(Graphics2D g) {
		g.drawImage(image, x - Background.instance.getCamX(), y - Background.instance.getCamY(), x + 64 - Background.instance.getCamX(), y + 64 - Background.instance.getCamY(), x1, y1, x2, y2, null);
	}

	public void startExplosionAnimation() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				int height = image.getHeight() / TILE_H;
				int width = image.getWidth() / TILE_W;
				for (int j = 0; j < TILE_H; j++) {
					for (int i = 0; i < TILE_W; i++) {
						x1 = i * width;
						y1 = j * height;
						x2 = (i + 1) * width;
						y2 = (j + 1) * height;
						try {
							Thread.sleep(10);
						} catch (InterruptedException ex) {
							Thread.currentThread().interrupt();
						}
					}
				}
				explosions.removeExplosion(instance);
			}
		}).start();
	}
}
