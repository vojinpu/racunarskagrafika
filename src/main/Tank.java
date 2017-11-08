package main;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import main.Game.GameStatus;
import rafgfxlib.Util;

public class Tank {
	private float x,y;
	private float startY,startX;

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
	
	private AffineTransform tankTransform = new AffineTransform();
	private AffineTransform turretTransform = new AffineTransform();
	
	public static Tank instance;
	
	public Tank(int x,int y){
		instance = this;
		
		//startParashuteAnimation();
		this.x = x;
		this.y = -20;
		startY = y;
		startX = x;
		mouseX = x + 600;
		mouseY = y;
		tank_with_parashute = Util.loadImage("/tank_with_parashute.png");
		tank_with_parashute = MyUtil.scaleSameProportion(tank_with_parashute, 0.5f);
		
		tankTransform.setToIdentity();
		tankTransform.translate(x, y);
		startParashuteAnimation();
	}
	
	public void setTankStartPos() {
		y = startY - Background.instance.getCamY();
		x = startX - Background.instance.getCamX();
		
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
		turretTransform.translate(tank.getWidth() / 3 * 2, tank.getHeight() / 2);
		turretTransform.rotate(turretAngle);
		turretTransform.translate(-tank_turret.getWidth() / 3 * 2, -tank_turret.getHeight() / 2);
	}
	public BufferedImage getTankImage(){
		if(Game.gameStatus == GameStatus.RUNNING || Game.gameStatus == GameStatus.ANIMATION) return tank;
		else return tank_with_parashute;
	}
	public BufferedImage getTankTurretImage(){
		return Game.gameStatus == GameStatus.RUNNING?tank_turret:null;
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

	public void moveForward(Background background){
		x = (float) (x + Math.cos(tankAngle) * tankMovementSpeed);
		y = (float) (y + Math.sin(tankAngle) * tankMovementSpeed);
		translateTank();
		if(!background.isTankAbleToMove((int)x- tank.getWidth() /2,(int) y- tank.getHeight() /2)){
			x = (float) (x - Math.cos(tankAngle) * tankMovementSpeed);
			y = (float) (y - Math.sin(tankAngle) * tankMovementSpeed);
			translateTank();
		}
	}
	
	public double getTurretAngle(){
		return turretAngle;
	}
	public double getTankAngle(){
		return tankAngle;
	}
	public void moveBackward(Background background){
		x = (float) (x - Math.cos(tankAngle) * tankMovementSpeed);
		y = (float) (y - Math.sin(tankAngle) * tankMovementSpeed);
		translateTank();
		if(!background.isTankAbleToMove((int)x- tank.getWidth() /2,(int) y- tank.getHeight() /2)){
			x = (float) (x + Math.cos(tankAngle) * tankMovementSpeed);
			y = (float) (y + Math.sin(tankAngle) * tankMovementSpeed);
			translateTank();
		}
	}
	
	public void move(double dX, double dY){
		x += dX;
		y += dY;
		if(tank==null || tank_turret == null)return;
		translateTank();
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
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
	
	public void rotateTunkClocwise(Background background){
		 tankAngle += tankRotateSpeed;
		 translateTank();
		 if(!background.isTankAbleToMove((int)x- tank.getWidth() /2,(int) y- tank.getHeight() /2)){
			 tankAngle -= tankRotateSpeed;
			 translateTank();
		 }
	}
	public void rotateTunkAntiClocwise(Background background){
		 tankAngle -= tankRotateSpeed;
		 translateTank();
		 if(!background.isTankAbleToMove((int)x- tank.getWidth() /2,(int) y- tank.getHeight() /2)){
			 tankAngle += tankRotateSpeed;
			 translateTank();
		 }
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
				Game.gameStatus = GameStatus.RUNNING;
			}
		}).start();
	}
	public void startWaveAnimation() {
		if(Game.gameStatus == GameStatus.ANIMATION)return;
		Game.gameStatus = GameStatus.ANIMATION;
		float startY = y;
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BufferedImage baseTank = Util.loadImage("/tank.png");
				baseTank = MyUtil.scale(baseTank, 180, 130);
				int k = 0;
				while (y <startY + 400) {
					k+=6;
					y+=10;
					tank = MyUtil.addWaves(baseTank, k);
					tank_turret = MyUtil.addWaves(tank_turret, k);
					translateTank();
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				}
				setTankStartPos();
				Game.gameStatus = GameStatus.RUNNING;
			}
		}).start();
	}
}
