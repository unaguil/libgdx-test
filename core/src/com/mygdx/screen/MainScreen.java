package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyGame;
import com.mygdx.screen.game.GameScreen;

// Esta clase define la funcionalidad de la pantalla del
// menú principal
public class MainScreen extends ScreenAdapter {

    // referencia a la aplicación
    // se usa para poder cambiar de pantallas
    private final MyGame game;

    private Skin skin;
    private Stage stage;
    private Table table;
    private SpriteBatch batch;
    private Texture background;

    private Music music;

    public MainScreen(MyGame game) {
        this.game = game;

        // se hace uso del grafo de escena
        // para estabelcer los widgets
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // los widgets requieren definir con qué imágenes se pinta
        // aquí se cargan los assets básicos para dibujarlos
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        
        // la distribución de los widgets en la pantalla se van a
        // distribuir utilizando una tabla que ocupa todo el espacio
        table = new Table();
        table.setFillParent(true);

        // añadimos la tabla al grafo de escena
        stage.addActor(table);

        // creamos un widget de tipo botón con el skin cargado anteriormente
        // el widget se añade a la tabla con unos tamaños mínimos y con un
        // espacio (padding) superior e inferior para situarlo un poco
        final TextButton startGameButton = new TextButton("Start game", skin);
		table.add(startGameButton).minWidth(200).padTop(200).padBottom(25);

        // escuchador para el click del botón "Start"
		startGameButton.addListener(new ChangeListener() {

			public void changed (ChangeEvent event, Actor actor) {
                // como resultado del click se destruye la pantalla actual
                // y se establece la del juego como pantalla visible
                MainScreen.this.dispose();
                MainScreen.this.game.setScreen(new GameScreen(MainScreen.this.game));
			}
            
		});

        // añadimos un botón para cambiar a la pantalla de opciones
        final TextButton optionsButton = new TextButton("Options", skin);
        table.row();
		table.add(optionsButton).minWidth(200).padBottom(25);

		optionsButton.addListener(new ChangeListener() {

			public void changed (ChangeEvent event, Actor actor) {
                MainScreen.this.dispose();
                MainScreen.this.game.setScreen(new OptionsScreen(MainScreen.this.game));
			}
            
		});

        // añadimos un botón para salir de la aplicación
        final TextButton exitButton = new TextButton("Exit game", skin);
        table.row();
		table.add(exitButton).minWidth(200);

		exitButton.addListener(new ChangeListener() {

			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.exit(); // cierra la aplicación completamente
			}
            
		});

        // creamos un batch para pintar el fondo de la pantalla
        batch = new SpriteBatch();
        background = new Texture("background.jpg");

        music = Gdx.audio.newMusic(Gdx.files.internal("music/main-music.mp3"));
        music.setLooping(true);
        music.play();
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

        // pintamos primer el fondo directamente
        batch.begin();
        batch.draw(background, 0, 0, 800, 600);
        batch.end();

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
        background.dispose();
        batch.dispose();
        music.dispose();
    }
}
