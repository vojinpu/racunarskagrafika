package main;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
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
		if(System.currentTimeMillis() - lastBulletTime > 150) {
		bullets.add(new Bullet(tank));
		lastBulletTime = System.currentTimeMillis();
		}
	}
	public void removeBullets() {
		bullets = new ArrayList<>();
	}
	public void drawBullets(Graphics2D g) {
		for (Bullet bullet : bullets) {
			g.drawImage(bullet.getBulletImage(), bullet.getTransform(), null);
		}
	}
	public void moveBullets(int width, int height, Background background, Explosions explosions) {
		if(bullets.size()==0)return;
		for(int i = bullets.size()-1;i >= 0;i--){
			Bullet bullet = bullets.get(i);
			bullet.moveBullet();
			if (bullet.checkCoallison(background, explosions)){
				bullets.remove(i);
				continue;
			}
			if(bullet.getX() < -bullet.getBulletImage().getWidth() || bullet.getY() < -bullet.getBulletImage().getHeight() ||
					bullet.getX() > width + bullet.getBulletImage().getWidth() || bullet.getY() > width + bullet.getBulletImage().getHeight()){
				bullets.remove(i);
			}
		}
	}
}
