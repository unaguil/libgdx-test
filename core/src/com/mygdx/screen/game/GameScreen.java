package com.mygdx.screen.game;

import java.io.IOException;
import java.nio.file.Paths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.controller.GameController;
import com.mygdx.controller.GameController.Direction;
import com.mygdx.game.MyGame;
import com.mygdx.screen.MainScreen;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;

public class GameScreen extends ScreenAdapter {

	class KeyboardProcessor extends InputAdapter {

		@Override
		public boolean keyUp(int key) {
			switch (key) {
				case Keys.DOWN:		gameController.move(Direction.DOWN);
									break;
				
				case Keys.UP:		gameController.move(Direction.UP);
									break;

				case Keys.RIGHT:	gameController.move(Direction.RIGHT);
									break;

				case Keys.LEFT:		gameController.move(Direction.LEFT);
									break;

				case Keys.ESCAPE:	showReturnDialog();
									break;

				case Keys.F:		toogleFullScreen();
									break;

				default:			break;
			}

			return false;
		}

		// muestra un diálogo para volver a la pantalla principal
		private void showReturnDialog() {
		
			Dialog dialog = new Dialog("Warning", skin, "dialog") {
				
				@Override
				public void result(Object obj) {
					boolean result = (boolean) obj;
					if (result) {
						game.getScreen().dispose();
						game.setScreen(new MainScreen(game));
					}
				}
	
			};
			
			dialog.text("Are you sure you want to return to main menu?");
			dialog.button("Yes", true); //sends "true" as the result
			dialog.button("No", false); //sends "false" as the result
			dialog.show(stage);
		}

		// cambia el modo gráfico entre pantalla completa y ventana
		private void toogleFullScreen() {
			if (game.isFullScreen()) {
				game.setWindowed();
			} else {
				game.setFullscreen();
			}
		}
	}

	private static final String SCREEN_NAME = "Game Screen";
	private static final int GAME_WIDTH = 800;
	private static final int GAME_HEIGHT = 600;

	private MyGame game;

	private Skin skin;
	private Stage stage;
	private MapActor mapActor;
	private PanelActor panelActor;
	private Group gameGroup;

	// mapa de juego
	private GameController gameController = new GameController();
	
	public GameScreen(MyGame game) {
		this.game = game;

		Gdx.app.log(SCREEN_NAME, "Iniciando screen principal del juego");

		stage = new Stage(new ExtendViewport(GAME_WIDTH, GAME_HEIGHT));
		
		// se carga la información del mapa desde fichero 
		try {
			gameController.loadFile(Paths.get("map/mapinfo.txt"));
		} catch (IOException e) {
			Gdx.app.log(SCREEN_NAME, "Could not load map file information");
		}

		// los widgets requieren definir con qué imágenes se pinta
        // aquí se cargan los assets básicos para dibujarlos
        skin = new Skin(Gdx.files.internal("uiskin.json"));

		// grupo para la pantalla de juego y el panel
		gameGroup = new Group();

		mapActor = new MapActor(gameController);
		gameGroup.addActor(mapActor);

		panelActor = new PanelActor(gameController);
		panelActor.setX(mapActor.getWidth());
		gameGroup.addActor(panelActor);

		stage.addActor(gameGroup);

		// establecemos un multiplexador para los eventos
		// queremos capturar eventos con el stage y con el listener
		// propio definido anteriormente
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(new KeyboardProcessor());

		// registramos el multiplexador de eventos como escuchador
		Gdx.input.setInputProcessor(multiplexer);

		gameController.restartMap();
	}

	float rotSpeed = 20.0f;

	@Override
	public void render(float delta) {
		// borrado de pantalla para empezar a dibujar
		ScreenUtils.clear(0, 0, 0, 1);

		stage.act(delta);
		stage.draw();
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		mapActor.dispose();
		stage.dispose();
	}
}
