package main;

import java.awt.Graphics2D;
import java.sql.Time;
import java.util.ArrayList;

public class BulletsList {
	Tank tank;
	ArrayList<Bullet> bullets;
	long lastBulletTime;
	public BulletsList(Tank tank) {
		bullets = new ArrayList<>();
		this.tank = tank;
	}
	public void addBullet() {
		if(System.currentTimeMillis() - lastBulletTime > 100) {
		bullets.add(new Bullet(tank));
		lastBulletTime = System.currentTimeMillis();
		}
	}
	public void drawBullets(Graphics2D g) {
		for (Bullet bullet : bullets) {
			g.drawImage(bullet.getBulletImage(), bullet.geTransform(), null);
		}
	}
	public void moveBullets(int width, int height) {
		ArrayList<Bullet> toBeRemoved = new ArrayList<>();
		for (Bullet bullet : bullets) {
			bullet.moveBullet();
			if(bullet.getX() < -bullet.getBulletImage().getWidth() || bullet.getY() < -bullet.getBulletImage().getHeight() ||
					bullet.getX() > width + bullet.getBulletImage().getWidth() || bullet.getY() > width + bullet.getBulletImage().getHeight()){
				toBeRemoved.add(bullet);
			}
		}
		for (Bullet bullet: toBeRemoved){
			bullets.remove(bullet);
		}
	}
}
