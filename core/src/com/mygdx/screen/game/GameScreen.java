package com.mygdx.screen.game;

import java.io.IOException;
import java.nio.file.Paths;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.controller.GameController;
import com.mygdx.controller.GameController.Direction;
import com.badlogic.gdx.InputAdapter;
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

				default:			break;
			}

			return false;
		}
	}

	private static final String SCREEN_NAME = "Game Screen";
	private static final int GAME_WIDTH = 800;
	private static final int GAME_HEIGHT = 600;

	private Stage stage;
	private MapActor mapActor;
	private PanelActor panelActor;

	// mapa de juego
	private GameController gameController = new GameController();
	
	public GameScreen(Game game) {
		Gdx.app.log(SCREEN_NAME, "Iniciando screen principal del juego");

		stage = new Stage(new ExtendViewport(GAME_WIDTH, GAME_HEIGHT));
		
		// se carga la información del mapa desde fichero 
		try {
			gameController.loadFile(Paths.get("map/mapinfo.txt"));
		} catch (IOException e) {
			Gdx.app.log(SCREEN_NAME, "Could not load map file information");
		}

		mapActor = new MapActor(gameController);
		stage.addActor(mapActor);

		panelActor = new PanelActor();
		stage.addActor(panelActor);

		// registramos el escuchador de teclado
		Gdx.input.setInputProcessor(new KeyboardProcessor());

		gameController.restartMap();
	}

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
