package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.screen.MainScreen;

// esta clase representa la aplicación del juego
// permite definir respuestas a distintos eventos
// aunque solamente 
public class MyGame extends Game {
    
    @Override
    public void create() {
        this.setScreen(new MainScreen(this));
    }
}
