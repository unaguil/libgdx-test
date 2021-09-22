package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGame;

// esta clase es el lanzador específico para escritorio
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		// establecemos el modo por defecto para la aplicación al inicio
		config.width = MyGame.DEFAULT_WIDTH;
		config.height = MyGame.DEFAULT_HEIGHT;
		
		new LwjglApplication(new MyGame(), config);
	}
}
