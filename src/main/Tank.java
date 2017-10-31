package main;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import rafgfxlib.Util;

public class Tank {
	private int x,y;
	private BufferedImage tank;
	private BufferedImage tank_turret;
	private double turretAngle;
	private double tankAngle;
	private int moveX;
	private int moveY;
	private double tankRotateSpeed = 0.05;
	private double turretRotateSpeed = 0.008;
	private double tankMovementSpeed = 5.0;
	private int mouseX = 800;
	private int mouseY = 200;
	
	private AffineTransform tankTransform = new AffineTransform();
	private AffineTransform turretTransform = new AffineTransform();
	
	public Tank(int x,int y){
		this.x = x;
		this.y = y;
		tank = Util.loadImage("/tank.png");
		tank_turret = Util.loadImage("/tank_turret.png");
		
		tank = MyUtil.scaleSameProportion(tank, 0.5f);
		tank_turret = MyUtil.scaleSameProportion(tank_turret, 0.5f);
		
		moveX = 2;
		moveY = 0;
		
		setInitialPositions();
		
	}
	
	public void setInitialPositions(){
		tankTransform.setToIdentity();
		tankTransform.translate(x, y);
		tankTransform.rotate(tankAngle + Math.PI);
		tankTransform.translate(-tank.getWidth() / 2, -tank.getHeight() / 2);
		
		turretTransform.setTransform(tankTransform);
		turretTransform.translate(tank.getWidth() / 3 *2, tank.getHeight() / 2);
		turretTransform.rotate(turretAngle);
		turretTransform.translate(-tank_turret.getWidth() / 3 *2, -tank_turret.getHeight() / 2);
	}
	public BufferedImage getTankImage(){
		return tank;
	}
	public BufferedImage getTankTurretImage(){
		return tank_turret;
	}
	public AffineTransform getTankTransform(){
		return tankTransform;
	}
	public AffineTransform getTurretTransform(){
		return turretTransform;
	}
	public void rotateTurret(int mouseX,int mouseY){
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		
		setTurretRotation();
	}
	
	private void setTurretRotation(){
		double deltaX = mouseX - x;
		double deltaY = mouseY - y;
		
		double absAngle = Math.atan2(deltaY, deltaX);
		
		turretAngle = absAngle - tankAngle;
		
		turretTransform.setTransform(tankTransform);
		turretTransform.translate(tank.getWidth() / 3 *2, tank.getHeight() / 2);
		turretTransform.rotate(turretAngle);
		turretTransform.translate(-tank_turret.getWidth() / 3 *2, -tank_turret.getHeight() / 2);
	}
	
	public void rotateTank(int mouseX,int mouseY){
		double deltaX = mouseX - x;
		double deltaY = mouseY - y;
		
		double absAngle = Math.atan2(deltaY, deltaX);
		
		turretAngle = absAngle - tankAngle;
		
		tankTransform.setToIdentity();
		tankTransform.translate(x, y);
		tankTransform.rotate(tankAngle + Math.PI);
		tankTransform.translate(-tank.getWidth() / 2, -tank.getHeight() / 2);
		
		turretTransform.setTransform(tankTransform);
		turretTransform.translate(tank.getWidth() / 3 *2, tank.getHeight() / 2);
		turretTransform.rotate(turretAngle);
		turretTransform.translate(-tank_turret.getWidth() / 3 *2, -tank_turret.getHeight() / 2);
	}
	
	public void moveForward(){
		x += Math.cos(tankAngle) * tankMovementSpeed;
		y += Math.sin(tankAngle) * tankMovementSpeed;
		
		translateTank();
	}
	
	public double getTurretAngle(){
		return turretAngle;
	}
	public double getTankAngle(){
		return tankAngle;
	}
	public void moveBackward(){
		x -= Math.cos(tankAngle) * tankMovementSpeed;
		y -= Math.sin(tankAngle) * tankMovementSpeed;
		
		translateTank();
	}
	
	public void move(double dX, double dY){
		x += dX;
		y += dY;
		
		translateTank();
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	private void translateTank(){
		tankTransform.setToIdentity();
		tankTransform.translate(x, y);
		tankTransform.rotate(tankAngle + Math.PI);
		tankTransform.translate(-tank.getWidth() / 2, -tank.getHeight() / 2);
		
		turretTransform.setTransform(tankTransform);
		turretTransform.translate(tank.getWidth() / 3 *2, tank.getHeight() / 2);
		turretTransform.rotate(turretAngle);
		turretTransform.translate(-tank_turret.getWidth() / 3 *2, -tank_turret.getHeight() / 2);
		
		setTurretRotation();
	}
	
	public void rotateTunkClocwise(){
		 tankAngle += tankRotateSpeed;
		 translateTank();
	}
	public void rotateTunkAntiClocwise(){
		 tankAngle -= tankRotateSpeed;
		 translateTank();
	}
	public void recoileBack(double dX,double dY) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				move(-dX,-dY);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				move(dX,dY);
			}
		}).start();
	}
}
