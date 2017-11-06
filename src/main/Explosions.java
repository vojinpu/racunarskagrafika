package main;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

public class Explosions {
	private ArrayList<Explosion> explosions;
	public Explosions() {
		// TODO Auto-generated constructor stub
		explosions = new ArrayList<>();
	}
	public void addExplosion(int x, int y){
		explosions.add(new Explosion(this,x,y));
	}
	public void removeExplosion(Explosion explosion) {
		explosions.remove(explosion);
	}
	public void drawExplosions(Graphics2D g){
		for(Explosion explosion : explosions)explosion.draw(g);
	}
}
