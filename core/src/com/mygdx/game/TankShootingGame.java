package com.mygdx.game;

import com.badlogic.gdx.Game;

public class TankShootingGame extends Game {
	GameScreen gameScreen;
	FirebaseInterface fb;

	public TankShootingGame(FirebaseInterface fb) {
		this.fb = fb;
	}

	@Override
	public void create() {
		this.gameScreen = new GameScreen(fb);
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
