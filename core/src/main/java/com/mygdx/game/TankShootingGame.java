package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.mygdx.game.network.GameClient;
import com.mygdx.game.network.PvPScreen;

public class TankShootingGame extends Game {
	public static final int LOADING_SCREEN = 0;
	public static final int MENU_SCREEN = 1;
	public static final int PVE_SCREEN = 2;
	public static final int PVP_SCREEN = 3;
	public static final int CREATE_ROOM_SCREEN = 4;
	public static final int PVP_CLIENT_SCREEN = 5;
	public static final int CONNECT_SERVER_SCREEN = 6;
	public static final int NUMBER_OF_SCREEN = 7;
	Screen oldScreen;

	public TankShootingGame() {
		GameClient gameClient = new GameClient("Client name");
	}

	@Override
	public void create() {
//		setScreen(new MainMenuScreen(this));
		this.changeScreen(MENU_SCREEN);
	}

	public void changeScreen(int screen) {
		if(screen < 0 || screen > NUMBER_OF_SCREEN - 1) return;

		oldScreen = this.getScreen();
		if(oldScreen != null) oldScreen.dispose();

		switch (screen){
			case MENU_SCREEN:
				this.setScreen(new MainMenuScreen(this));
				break;
			case PVE_SCREEN:
				this.setScreen(new PvEScreen());
				break;
			case PVP_SCREEN:
				this.setScreen(new PvPScreen());
				break;
			case CREATE_ROOM_SCREEN:
				this.setScreen(new CreateRoomScreen(this));
				break;
			case CONNECT_SERVER_SCREEN:
				this.setScreen(new ConnectServerScreen(this));
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
//		this.screen.dispose();
	}

	@Override
	public void resize(int width, int height) {
		//gameScreen.resize(width, height);
//		this.screen.resize(width, height);
	}
}
