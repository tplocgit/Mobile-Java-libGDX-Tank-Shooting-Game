package main.java.com.mygdx.launcher;

import android.os.Bundle;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.TankShootingGame;
import com.mygdx.game.network.GameClient;
import com.mygdx.game.network.GameServer;

import java.net.InetAddress;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		if(GameServer.getInstance() != null) {
			GameServer.getInstance().shutdownServer();
		}

		if(GameClient.getInstance() != null) {
			GameClient.getInstance().shutdownClient();
		}

		initialize(new TankShootingGame(), config);

	}
}//new AndroidInterface()
