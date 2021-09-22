package es.deusto.prog3.captureflag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import es.deusto.prog3.captureflag.screen.MainScreen;

import com.badlogic.gdx.utils.viewport.FitViewport;

// esta clase representa la aplicación del juego
// permite definir respuestas a distintos eventos
// aunque solamente 
public class CaptureTheFlag extends Game {

    // resolución por defecto del juego en modo ventana
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;

    // indica si el juego se encuentra o no en modo ventana
    private boolean fullScreen = false;

    // viewport utilizado en las diferentes pantallas del juego
    private Viewport viewport;

    // skin para los widgets utilizados en el juego
    private Skin skin;

    // este método se llama cuando se crea la aplicación
    // principal del juego
    @Override
    public void create() {
        // viewport por defecto que puede ser reutilizado
        viewport = new FitViewport(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // los widgets requieren definir con qué imágenes se pinta
        // aquí se cargan los assets básicos para dibujar los botones
        skin = new Skin(Gdx.files.internal("widgets/uiskin.json"));
        
        // se establece la ventana inicial al comenzar
        this.setScreen(new MainScreen(this));
    }

    // permite obtener y reutilizar el mismo viewport en las 
    // diferentes pantallas del juego
    public Viewport getViewport() {
        return viewport;
    }

    // obtiene el skin por defecto para los widgets de
    // toda la aplicación
    public Skin getDefaultSkin() {
        return skin;
    }

    // establece el modo gráfico a ventana completa
    // utilizando la resolución actual del escritorio
    public void setFullscreen() {
        Monitor currMonitor = Gdx.graphics.getPrimaryMonitor();
        DisplayMode displayMode = Gdx.graphics.getDisplayMode(currMonitor);
        
        if(!Gdx.graphics.setFullscreenMode(displayMode)) {
            Gdx.app.log(CaptureTheFlag.class.getName(), "Could not change screen mode to full screen");
        } else {
            fullScreen = true;
        }
    }

    // establece el modo en ventana utilizando
    // el modo por defecto
    public void setWindowed() {
        Gdx.graphics.setWindowedMode(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        fullScreen = false;
    }

    // indica si el modo está establecido en pantalla completa
    public boolean isFullScreen() {
        return fullScreen;
    }

    @Override
    public void dispose() {
        skin.dispose();
    }
}
