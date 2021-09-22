package com.mygdx.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.controller.GameController;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PanelActor extends Actor {

    private static final int PANEL_WIDTH = 200;
    private static final int PANEL_HEIGHT = 600;

    private static final int COUNTER_X_POS = 50;
    private static final int COUNTER_Y_POS = 505;

    private TextureRegion panel;
    private BitmapFont font;

    private GameController gameController;

    public PanelActor(GameController gameController) {
        // referencia para acceder a informción del juego
        this.gameController = gameController;

        // cargamos la imagen para el panel
        panel = new TextureRegion(new Texture("panel.png"));

        // establecemos un tamaño por defecto para el actor
        setWidth(PANEL_WIDTH);
        setHeight(PANEL_HEIGHT);

        // cargamos el bitmap de la fuente desde los recursos
		font = new BitmapFont(Gdx.files.internal("arial.fnt"), false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // dibujamos el fondo del panel
        batch.draw(panel, 
            getX(), getY(), getOriginX(), getOriginY(),
            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation()
        );

        // dibujamos el texto en pantalla
		// en una posición situada a la derecha del mapa
		String counterLabel = String.format("%d flags", gameController.getCapturedFlags());
		font.draw(batch, counterLabel, getX() + COUNTER_X_POS, getY() + COUNTER_Y_POS);
    }

    // destruye los recursos utilizados
    public void dispose() {
        panel.getTexture().dispose();
        font.dispose();
    }
}
