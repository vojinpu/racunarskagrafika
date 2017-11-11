package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import rafgfxlib.Util;

public class EndScene {
	Game game;
	double rotation = 0;
	BufferedImage pozadina;
	BufferedImage image_source;
	AffineTransform transform;
	ArrayList<Kvadrat>kvadratici;
	ArrayList<Kvadrat>pomerajuce_kockice;
	
	float size = 1;
	public EndScene(Game game){
		this.game = game;
		
	}
	
	static class Kvadrat {
		int x,y;
		BufferedImage slika;
		
	}
	
	public void crtanje(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, game.width, game.height);
		g.drawImage(pozadina, 0,0,null);
		
		for(int i=0;i<kvadratici.size();i++) {
			Kvadrat kvadrat=kvadratici.get(i);
			//System.out.println("kvadrat " + i + " " + kvadrat.x + " " + kvadrat.y);
			g.drawImage(kvadrat.slika, kvadrat.x, kvadrat.y, null);
		}
	
		if(pomerajuce_kockice != null && pomerajuce_kockice.size()>0){
			for(int i=0;i<pomerajuce_kockice.size();i++) {
				Kvadrat kvadrat=pomerajuce_kockice.get(i);
				//System.out.println("kvadrat " + i + " " + kvadrat.x + " " + kvadrat.y);
				g.drawImage(kvadrat.slika, kvadrat.x, kvadrat.y, null);
			}
		}
		
	}
	
	public void napraviKockice(){
		createScreenShot();
		BufferedImage maska= Util.loadImage("/gameover.png");
		WritableRaster raster= pozadina.getRaster();
		WritableRaster maskaRaster=maska.getRaster();
		kvadratici= new ArrayList<>();
		for(int y=0;y<maskaRaster.getHeight();y++) {
			if(y%2==0){
			for(int x=0;x<maskaRaster.getWidth();x++) {
				int[] boja=new int[3];
				maskaRaster.getPixel(x, y, boja);
				if(boja[0]==0) {
					Kvadrat kvadrat=new Kvadrat();
					kvadrat.x=pozadina.getWidth()/maska.getWidth()*x;
					kvadrat.y= pozadina.getHeight()/maska.getHeight()*y;
					WritableRaster rasterKocka = Util.createRaster(pozadina.getWidth()/maska.getWidth(), pozadina.getHeight()/maska.getHeight(), false);
					kvadratici.add(kvadrat);
					for(int x1=0;x1<pozadina.getWidth()/maska.getWidth();x1++) {
						for(int y1=0;y1<pozadina.getHeight()/maska.getHeight();y1++) {
							raster.getPixel(x1 + kvadrat.x, y1 + kvadrat.y, boja);
							rasterKocka.setPixel(x1,y1,boja);
							boja[0]=0;
							boja[1]=0;
							boja[2]=0;
							raster.setPixel(x1 + kvadrat.x, y1 + kvadrat.y, boja);
						}
					}
					kvadrat.slika = Util.rasterToImage(rasterKocka);
				}
			}
			} else {
				for(int x=maskaRaster.getWidth()-1;x>=0;x--) {
					int[] boja=new int[3];
					maskaRaster.getPixel(x, y, boja);
					if(boja[0]==0) {
						Kvadrat kvadrat=new Kvadrat();
						kvadrat.x=pozadina.getWidth()/maska.getWidth()*x;
						kvadrat.y= pozadina.getHeight()/maska.getHeight()*y;
						WritableRaster rasterKocka = Util.createRaster(pozadina.getWidth()/maska.getWidth(), pozadina.getHeight()/maska.getHeight(), false);
						kvadratici.add(kvadrat);
						for(int x1=0;x1<pozadina.getWidth()/maska.getWidth();x1++) {
							for(int y1=0;y1<pozadina.getHeight()/maska.getHeight();y1++) {
								raster.getPixel(x1 + kvadrat.x, y1 + kvadrat.y, boja);
								rasterKocka.setPixel(x1,y1,boja);
								boja[0]=0;
								boja[1]=0;
								boja[2]=0;
								raster.setPixel(x1 + kvadrat.x, y1 + kvadrat.y, boja);
							}
						}
						kvadrat.slika = Util.rasterToImage(rasterKocka);
					}
				}
			}
		}
		Collections.reverse(kvadratici);
		pozadina=Util.rasterToImage(raster);
	}
	
	public void createScreenShot(){
		image_source = new BufferedImage(game.getWidth(), game.getHeight(), BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = image_source.createGraphics();
	    
	    game.render(g, 0, 0);
	    
	    pozadina = image_source;

	}
	
	public void pokreniPomeranje() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				ArrayList<Kvadrat>pomerajuci=new ArrayList<>();
				for (Kvadrat k: kvadratici) {
					pomerajuci.add(k);
				}
				int i = 0;
				int brojac=4;
				pomerajuce_kockice = new ArrayList<>();
				while(kvadratici.size()>0) {
					
					brojac++;
					if(brojac==5) {
						if(i<pomerajuci.size()-1)i++;
						pomerajuce_kockice.add(pomerajuci.get(i));
						brojac = 0;
					}
					
					for(int j = 0;j <= i;j++) {
						Kvadrat k = pomerajuci.get(j);
						if(k.y<game.height)k.y+=2;
						else {
							kvadratici.remove(k);
						}
						
					}
					System.out.println("eve odje");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("kraj");
				//pokreni ponovo igru
			}
		}).start();
	}
}
