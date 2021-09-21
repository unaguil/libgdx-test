package com.mygdx.screen;

import java.io.IOException;
import java.nio.file.Paths;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.screen.gamemap.Cell;
import com.mygdx.screen.gamemap.GameController;
import com.mygdx.screen.gamemap.GameController.Direction;
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
	private static final int TILE_SIZE = 40;

	private static final int MAP_WIDTH = TILE_SIZE * 15;
	private static final int MAP_HEIGHT = TILE_SIZE * 15;

	private Game game;

	private Viewport viewport;
	private SpriteBatch batch;
	private Texture ground;
	private Texture tree;
	private Texture mountain;
	private Texture player;
	private Texture flag;
	private Texture panel;

	private BitmapFont font;

	// mapa de juego
	private GameController gameController = new GameController();
	
	public GameScreen(Game game) {
		Gdx.app.log(SCREEN_NAME, "Iniciando screen principal del juego");

		this.game = game;

		viewport = new ExtendViewport(GAME_WIDTH, GAME_HEIGHT);

		batch = new SpriteBatch();
		ground = new Texture("gamemap/ground.png");
		tree = new Texture("gamemap/tree.png");
		mountain = new Texture("gamemap/mountain.png");
		flag = new Texture("gamemap/flag.png");
		player = new Texture("gamemap/player.png");

		panel = new Texture("panel.png");
		
		// se carga la información del mapa desde fichero 
		try {
			gameController.loadFile(Paths.get("gamemap/mapinfo.txt"));
		} catch (IOException e) {
			Gdx.app.log(SCREEN_NAME, "Could not load map file information");
		}

		// cargamos el bitmap de la fuente desde los recursos
		font = new BitmapFont(Gdx.files.internal("arial.fnt"), false);

		// registramos el escuchador de teclado
		Gdx.input.setInputProcessor(new KeyboardProcessor());

		gameController.restartMap();
	}

	@Override
	public void render(float delta) {
		// borrado de pantalla para empezar a dibujar el fotograma
		ScreenUtils.clear(0, 0, 0, 1);

		// actualizar la cámara y establecerla antes de dibujar
		viewport.getCamera().update();
		batch.setProjectionMatrix(viewport.getCamera().combined);

		batch.begin(); // requerido para empezar a dibujar

		// aqui vamos a dibujar en pantalla cada baldosa
		// desde la esquina superior izquierda
		for (int row = 0; row < gameController.getRows(); row++) {
			for (int column = 0; column < gameController.getColumns(); column++) {
				// posición actual de dibujado
				// tenemos en cuenta que el eje y está invertido
				// además, la esquina de dibujado de la imagen es la inferior izquierda
				int x = columnToX(column);
				int y = rowToY(row);
				
				switch (gameController.getCellType(new Cell(row, column))) {
					case GROUND:	batch.draw(ground, x, y, TILE_SIZE, TILE_SIZE);
									break;

					case MOUNTAIN:	batch.draw(ground, x, y, TILE_SIZE, TILE_SIZE);
									batch.draw(mountain, x, y, TILE_SIZE, TILE_SIZE);
									break;

					case TREE:		batch.draw(ground, x, y, TILE_SIZE, TILE_SIZE);
									batch.draw(tree, x, y, TILE_SIZE, TILE_SIZE);
									break;

					case FLAG:		batch.draw(ground, x, y, TILE_SIZE, TILE_SIZE);
									batch.draw(flag, x, y, TILE_SIZE, TILE_SIZE);
									break;

					default: 		break;
				}
			}
		}
		
		// dibujamos el personaje en su posición actual
		int playerXPos = columnToX(gameController.getPlayer().getColumn());
		int playerYPos = rowToY(gameController.getPlayer().getRow());
		batch.draw(player, playerXPos, playerYPos, TILE_SIZE, TILE_SIZE);

		// dibujamos el panel de la derecha
		batch.draw(panel, MAP_WIDTH, 0);

		// dibujamos el texto en pantalla
		// en una posición situada a la derecha del mapa
		String counterLabel = String.format("%d flags", gameController.getCapturedFlags());
		font.draw(batch, counterLabel, MAP_WIDTH + 50, Gdx.graphics.getHeight() - 95);
		
		batch.end(); // esto es necesario para terminar el dibujado
	}

	// convierte la fila a píxeles
	public int rowToY(int row) {
		return Gdx.graphics.getHeight() - 1 - (TILE_SIZE * (row + 1));
	}

	// convierte la columna a píxeles
	public int columnToX(int column) {
		return column * TILE_SIZE;
	}
	
	@Override
	public void dispose() {
		Gdx.app.log(SCREEN_NAME, "Destruyendo la screen principal del juego");
		batch.dispose();
		
		ground.dispose();
		tree.dispose();
		mountain.dispose();
		player.dispose();
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log(SCREEN_NAME, String.format("Screen: %d x %d", width, height));
		viewport.update(width, height);
		viewport.getCamera().position.set(GAME_WIDTH / 2f, GAME_HEIGHT / 2f, 0);
	}
}
