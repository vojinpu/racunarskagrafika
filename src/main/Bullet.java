package main;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import rafgfxlib.Util;

public class Bullet {
	private Tank tank;
	private AffineTransform transform;
	private BufferedImage bullet;
	private double bulletSpeed = 30;
	private double angle;
	private double x, y;

	private double dX, dY;

	public Bullet(Tank tank) {
		bullet = Util.loadImage("/bullet.png");
		bullet = MyUtil.scaleSameProportion(bullet, 0.7f);
		this.tank = tank;
		x = tank.getTurretTransform().getTranslateX() - bullet.getWidth() / 2 + Background.instance.getCamX();
		y = tank.getTurretTransform().getTranslateY() - bullet.getHeight() / 2 + Background.instance.getCamY();

		angle = tank.getTurretAngle() + tank.getTankAngle();

		if (angle < -Math.PI / 2 || angle > Math.PI / 2) {
			bullet = MyUtil.horiozntalFlip(bullet);
		}
		dX = Math.cos(angle) * bulletSpeed;
		dY = Math.sin(angle) * bulletSpeed;

		x += dX;
		y += dY;

		transform = new AffineTransform();
		transform.setToIdentity();
		transform.translate(x - Background.instance.getCamX(), y - Background.instance.getCamY());

		tank.recoileBack(dX / 5, dY / 5);
	}

	public BufferedImage getBulletImage() {
		return bullet;
	}

	public AffineTransform getTransform() {
		return transform;
	}

	public void moveBullet() {
		x += dX;
		y += dY;

		// System.out.println(x+","+y+","+dX+","+dY);

		transform.setToIdentity();
		transform.translate(x - Background.instance.getCamX(), y - Background.instance.getCamY());
		if (angle > -Math.PI / 2 && angle < Math.PI / 2) {
			transform.rotate(angle, bullet.getWidth() / 2, bullet.getHeight() / 2);
			transform.translate(-bullet.getWidth() / 2, -bullet.getHeight());
		} else {
			transform.rotate(angle + Math.PI, bullet.getWidth() / 2, bullet.getHeight() / 2);
			transform.translate(bullet.getWidth() / 2, bullet.getHeight());
		}
	}

	public boolean checkCoallison(Background background, Explosions explosions) {
		for (int i = 0; i < background.getMapH(); ++i) {
			for (int j = 0; j < background.getMapW(); ++j) {
				if (background.getTileMap()[j][i] == 10) {
					int x2 = j * Background.getTileW() - background.getCamX();
					int y2 = i * Background.getTileH() - background.getCamY();
					boolean coallision = MyUtil.checkCoallison((int) x - Background.instance.getCamX(),
							(int) y - Background.instance.getCamY(), x2, y2, bullet.getWidth(), bullet.getHeight(),
							Background.getTileW(), Background.getTileH(), transform);
					if (coallision) {
						explosions.addExplosion(x2, y2);
						background.getTileMap()[j][i] = 11;
						background.decriseTargets();
						return true;
					}
				}
			}
		}
		return false;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
