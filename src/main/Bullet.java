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
	
	private double dX,dY;
	
	public Bullet(Tank tank){
		bullet = Util.loadImage("/bullet.png");
		bullet = MyUtil.scaleSameProportion(bullet, 0.7f);
		this.tank = tank;
		x = tank.getX();
		y = tank.getY();
	
		angle = tank.getTurretAngle() + tank.getTankAngle();
		
		if(angle < -Math.PI/2 || angle > Math.PI /2) {
			bullet = MyUtil.horiozntalFlip(bullet);
		}
		dX = Math.cos(angle) * bulletSpeed;
		dY = Math.sin(angle) * bulletSpeed;
		
		x +=dX *2;
		y +=dY *2;
		
		transform = new AffineTransform();
		transform.setToIdentity();
		transform.setTransform(tank.getTurretTransform());
		transform.translate(x, y);
		
		tank.recoileBack(dX/5,dY/5);
	}
	public BufferedImage getBulletImage(){
		return bullet;
	}
	public AffineTransform geTransform(){
		return transform;
	}
	
	public void moveBullet(){
		x += dX;
		y += dY;
		
		//System.out.println(x+","+y+","+dX+","+dY);
		
		transform.setToIdentity();
		transform.translate(x - bullet.getWidth(), y - bullet.getHeight() /2);
		if(angle > -Math.PI/2 && angle < Math.PI /2) {
			transform.rotate(angle);
		} else {
			transform.rotate(angle + Math.PI );
		}
		
		//transform.translate(-bullet.getWidth() / 2, -bullet.getHeight() / 2);
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
}
