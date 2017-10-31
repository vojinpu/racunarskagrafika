package main;

public class Main {
	public static void main(String[] args){
		Game game = new Game("Game", 640, 640);
		Background back = new Background();
//		back.initGameWindow();
		game.initGameWindow();
	}
}
