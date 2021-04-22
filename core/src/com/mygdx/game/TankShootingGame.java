package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class TankShootingGame extends Game {
	public static final int LOADING_SCREEN = 0;
	public static final int MENU_SCREEN = 1;
	public static final int PVE_SCREEN = 2;
	public static final int PVP_SCREEN = 3;
	public static final int NUMBER_OF_SCREEN = 4;
	Screen oldScreen;
	GameScreen pveScreen;
	PvPScreen pvpScreen;
	MainMenuScreen menuScreen;
	FirebaseInterface myFb;

	public TankShootingGame(FirebaseInterface fb) {
		myFb = fb;
	}

	@Override
	public void create() {
		setScreen(new MainMenuScreen(this));
//
	}

	public void changeScreen(int screen) {
		if(screen < 0 || screen > NUMBER_OF_SCREEN - 1) return;

		oldScreen = this.getScreen();
		if(oldScreen != null) oldScreen.dispose();

		switch (screen){
			case MENU_SCREEN:
				if (menuScreen == null) menuScreen = new MainMenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case PVE_SCREEN:
				if(pveScreen == null) pveScreen = new GameScreen(myFb);
				this.setScreen(pveScreen);
				break;
			case PVP_SCREEN:
				if(pvpScreen == null) pvpScreen = new PvPScreen(myFb);
				this.setScreen(pvpScreen);
				break;
		}
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		//gameScreen.dispose();
		this.screen.dispose();
	}

	@Override
	public void resize(int width, int height) {
		//gameScreen.resize(width, height);
		this.screen.resize(width, height);
	}
}
