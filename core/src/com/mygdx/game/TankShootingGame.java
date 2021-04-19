package com.mygdx.game;

import com.badlogic.gdx.Game;

public class TankShootingGame extends Game {
	GameScreen gameScreen;
	FirebaseInterface myFb;

	public TankShootingGame(FirebaseInterface fb) {
		myFb = fb;
	}

	@Override
	public void create() {
		this.gameScreen = new GameScreen(myFb);
		setScreen(this.gameScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		gameScreen.dispose();
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width, height);
	}
}
