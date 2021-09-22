package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.mygdx.screen.MainScreen;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.Graphics.DisplayMode;;

// esta clase representa la aplicación del juego
// permite definir respuestas a distintos eventos
// aunque solamente 
public class MyGame extends Game {

    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;

    private boolean fullScreen = false;

    @Override
    public void create() {
        this.setScreen(new MainScreen(this));
    }

    // establece el modo gráfico a ventana completa
    // utilizando la resolución actual del escritorio
    public void setFullscreen() {
        Monitor currMonitor = Gdx.graphics.getPrimaryMonitor();
        DisplayMode displayMode = Gdx.graphics.getDisplayMode(currMonitor);
        
        if(!Gdx.graphics.setFullscreenMode(displayMode)) {
            Gdx.app.log(MyGame.class.getName(), "Could not change screen mode to full screen");
        } else {
            fullScreen = true;
        }
    }

    // establece el modo en ventana utilizando
    // el modo por defecto
    public void setWindowed() {
        Gdx.graphics.setWindowedMode(MyGame.DEFAULT_WIDTH, MyGame.DEFAULT_HEIGHT);
        fullScreen = false;
    }

    // indica si el modo está establecido en pantalla completa
    public boolean isFullScreen() {
        return fullScreen;
    }
}
