package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class EndScene {
	Game game;
	double rotation = 0;
	BufferedImage image;
	BufferedImage image_source;
	AffineTransform transform;
	float size = 1;
	public EndScene(Game game){
		this.game = game;
	}
	public void createScreenShot(){
		image_source = new BufferedImage(game.getWidth(), game.getHeight(), BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = image_source.createGraphics();
	    
	    game.render(g, 0, 0);
	    
	    image = image_source;
	    
		transform = new AffineTransform();
		//transform.rotate(Math.PI, image.getWidth()/2,image.getHeight()/2);
	}
	public void startEndAnimation(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(size>0.01){
					
					image = MyUtil.scaleSameProportion(image_source, size);
					
					transform = new AffineTransform();
					transform.rotate(rotation, image.getWidth()/2,image.getHeight()/2);
					
					size-= 0.001f;
					rotation += 0.1;
					
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
