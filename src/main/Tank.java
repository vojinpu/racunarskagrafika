package main;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import rafgfxlib.Util;

public class Tank {
	private int x,y;
	private int startY;
	private BufferedImage tank;
	private BufferedImage tank_with_parashute;
	private BufferedImage tank_turret;
	private double turretAngle;
	private double tankAngle;
	private int moveX;
	private int moveY;
	private double tankRotateSpeed = 0.05;
	private double turretRotateSpeed = 0.008;
	private double tankMovementSpeed = 5.0;
	private int mouseX;
	private int mouseY;
	private double parashuteAngle;
	private double parashuteRotationSpeed = 0.019;
	private double parashuteRotationAcceleration = -0.001;
	
	public boolean startGame = false;
	
	private AffineTransform tankTransform = new AffineTransform();
	private AffineTransform turretTransform = new AffineTransform();
	
	public Tank(int x,int y){
		
		//startParashuteAnimation();
		this.x = x;
		this.y = -20;
		startY = y;
		mouseX = x + 600;
		mouseY = y;
		tank_with_parashute = Util.loadImage("/tank_with_parashute.png");
		tank_with_parashute = MyUtil.scaleSameProportion(tank_with_parashute, 0.5f);
		
		tankTransform.setToIdentity();
		tankTransform.translate(x, y);
		startParashuteAnimation();
	}
	
	public void setTankStartPos() {
		y = startY;
		
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
		if(startGame) return tank;
		else return tank_with_parashute;
	}
	public BufferedImage getTankTurretImage(){
		return startGame==true?tank_turret:null;
	}
	public AffineTransform getTankTransform(){
		return tankTransform;
	}
	public AffineTransform getTurretTransform(){
		return turretTransform;
	}
	public void rotateTurret(int mouseX,int mouseY){
		if(tank_turret==null)return;
		
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
		if(tank==null || tank_turret == null)return;
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
	
	public void moveParashute() {
		y+=2;
		tankTransform.setToIdentity();
		tankTransform.translate(x, y);
		parashuteRotationSpeed+=parashuteRotationAcceleration;
		if(parashuteRotationSpeed< -0.02 || parashuteRotationSpeed > 0.02) {
			parashuteRotationAcceleration*=-1;
		}
		parashuteAngle += parashuteRotationSpeed;
		tankTransform.rotate(parashuteAngle + Math.PI );
		tankTransform.translate(-tank_with_parashute.getWidth() / 2, -tank_with_parashute.getHeight() / 2);
	}
	public void startParashuteAnimation() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (y <startY - 100) {
					moveParashute();
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				}
				setTankStartPos();
				startGame = true;
			}
		}).start();
	}
}
