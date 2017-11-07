package Plamen;

import java.util.Random;

public class Iskra {

	private int x, y, speed;
	private float alphaSpeed;
	private float alpha;
	private Random r;

	public Iskra() {
		r = new Random();
		generate();
		
	}
	
	private void generate() {
		y = 0;
		x = (int)( (r.nextGaussian() * 5 * 1.8 + 32) );
		alpha = 1.0f;
		alphaSpeed = (r.nextInt(10) + 1) / 30.0f;
//		alphaSpeed = 0.2f;
		while(x < 0)
			x += 10;
		
		while(x >= 64)
			x -= 10;
		
		speed = r.nextInt(4) + 3;

	}
	
	
	public void update(){
		if(y < 64){
			y += speed;
		alpha -= alphaSpeed;
		
		if(alpha < 0)
			alpha = 0;
		
		}
		
		else
			generate();
		
	}
	

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public float getAlpha() {
		return alpha;
	}
	
	

}
