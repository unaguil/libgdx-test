package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.screen.GameScreen;

public class MyGame extends Game {
    
    @Override
    public void create() {
        this.setScreen(new GameScreen(this));
    }
}
