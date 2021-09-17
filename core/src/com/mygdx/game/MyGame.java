package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.screen.MainScreen;

public class MyGame extends Game {
    
    @Override
    public void create() {
        this.setScreen(new MainScreen(this));
    }
}
