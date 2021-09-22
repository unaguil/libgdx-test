package es.deusto.prog3.captureflag.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import es.deusto.prog3.captureflag.CaptureTheFlag;

// esta clase es el lanzador específico para escritorio
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		// establecemos el modo por defecto para la aplicación al inicio
		config.width = CaptureTheFlag.DEFAULT_WIDTH;
		config.height = CaptureTheFlag.DEFAULT_HEIGHT;
		config.title = "Capture The Flag v0.1";
		
		new LwjglApplication(new CaptureTheFlag(), config);
	}
}
