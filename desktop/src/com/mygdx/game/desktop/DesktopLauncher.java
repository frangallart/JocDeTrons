package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.JocDeTrons;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "Joc de Trons";
        config.width = 800;
        config.height = 520;

        new LwjglApplication(new JocDeTrons("Joc de Trons"), config);
	}
}
