package com.mygdx.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PanelActor extends Actor {

    private Texture panel;
    private BitmapFont font;

    public PanelActor() {
        // cargamos la imagen para el panel
        panel = new Texture("panel.png");

        // cargamos el bitmap de la fuente desde los recursos
		font = new BitmapFont(Gdx.files.internal("arial.fnt"), false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Gdx.app.log()
        batch.draw(new TextureRegion(panel), 
            getX(), getY(), getOriginX(), getOriginY(),
            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation()
        );
    }

    public void dispose() {
        panel.dispose();
        font.dispose();
    }
}
