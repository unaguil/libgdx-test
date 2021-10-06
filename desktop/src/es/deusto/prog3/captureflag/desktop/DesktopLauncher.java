package es.deusto.prog3.captureflag.desktop;

import com.badlogic.gdx.backends.lwjgl3.*;

import es.deusto.prog3.captureflag.CaptureTheFlag;

// esta clase es el lanzador específico para escritorio
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		
		// establecemos el modo por defecto para la aplicación al inicio
		config.setWindowedMode(CaptureTheFlag.DEFAULT_WIDTH, CaptureTheFlag.DEFAULT_HEIGHT);
		config.setTitle("Capture The Flag v0.1");
		
		new Lwjgl3Application(new CaptureTheFlag(), config);
	}
}
