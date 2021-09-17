package com.mygdx.screen;

import com.badlogic.gdx.Gdx;

import java.lang.StackWalker.Option;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

// Esta clase define la funcionalidad de la pantalla
// de opciones del juego
public class OptionsScreen extends ScreenAdapter {

    // referencia a la aplicación
    // se usa para poder cambiar de pantallas
    private final Game game;

    private Skin skin;
    private Stage stage;
    private Table table;

    public OptionsScreen(Game game) {
        this.game = game;

        // se hace uso del grafo de escena
        // para estabelcer los widgets
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // los widgets requieren definir con qué imágenes se pinta
        // aquí se cargan los assets básicos para dibujar los botones
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        
        // la distribución de los widgets en la pantalla se van a
        // distribuir utilizando una tabla que ocupa todo el espacio
        table = new Table();
        table.setFillParent(true);

        // podemos activar esta opción para ver líneas de ayuda
        //table.setDebug(true);

        // añadimos la tabla al grafo de escena
        stage.addActor(table);

        // creamos un widget de tipo botón con el skin cargado anteriormente
        // el widget se añade a la tabla con unos tamaños mínimos y con un
        // espacio (padding) superior e inferior para situarlo un poco
        final CheckBox fullScreenCheck = new CheckBox("Fullscreen", skin);
		table.add(fullScreenCheck).minWidth(200).padTop(100).padBottom(25);

        // añadimos un segundo botón para volver al menú principal
        final TextButton exitButton = new TextButton("Return", skin);
        table.row();
		table.add(exitButton).minWidth(200);

		exitButton.addListener(new ChangeListener() {

			public void changed (ChangeEvent event, Actor actor) {
				OptionsScreen.this.dispose();
                OptionsScreen.this.game.setScreen(new MainScreen(OptionsScreen.this.game));
			}
            
		});
    }

    // este método actualiza el viewport cuando se ajusta
    // el tamaño de la ventana
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // aquí hacemos uso del grafo de escena para
        // los widgets
        stage.act(delta);
        stage.draw();
    }

    // método para destruir los recursos cargados
    // para esta pantalla. 
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
