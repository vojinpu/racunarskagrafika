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
		x = tank.getTurretTransform().getTranslateX() - bullet.getWidth() /2;
		y = tank.getTurretTransform().getTranslateY() - bullet.getHeight() /2;
	
		angle = tank.getTurretAngle() + tank.getTankAngle();
		
		if(angle < -Math.PI/2 || angle > Math.PI /2) {
			bullet = MyUtil.horiozntalFlip(bullet);
		}
		dX = Math.cos(angle) * bulletSpeed;
		dY = Math.sin(angle) * bulletSpeed;
		
		x +=dX;
		y +=dY;
		
		transform = new AffineTransform();
		transform.setToIdentity();
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
		transform.translate(x , y );
		if(angle > -Math.PI/2 && angle < Math.PI /2) {
			transform.rotate(angle,bullet.getWidth()/2, bullet.getHeight()/2);
			transform.translate(- bullet.getWidth() / 2 , - bullet.getHeight()  );
		} else {
			transform.rotate(angle + Math.PI, bullet.getWidth()/2,bullet.getHeight()/2);
			transform.translate(bullet.getWidth() / 2 ,  bullet.getHeight()  );
		}
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
}
