package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screen.SimpleScreen;

public class MyGame extends Game {

    @Override
    public void create() {
        this.setScreen(new SimpleScreen());
    }
    
}
