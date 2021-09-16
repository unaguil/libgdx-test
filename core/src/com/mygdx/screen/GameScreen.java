package com.mygdx.screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.io.FileReader;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import java.util.Random;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class GameScreen extends ScreenAdapter {

	class KeyboardProcessor extends InputAdapter {

		@Override
		public boolean keyUp(int key) {
			// se guardan las posiciones anteriores por si
			// es necesario restaurar la posición
			int previousRow = playerRow;
			int previousColumn = playerColumn;

			switch (key) {
				case Keys.DOWN:		playerRow++;
									break;
				
				case Keys.UP:		playerRow--;
									break;

				case Keys.RIGHT:	playerColumn++;
									break;

				case Keys.LEFT:		playerColumn--;
									break;

				default:			break;
			}

			// se comprueba la nueva posición en el mapa
			// si es inválida se restaura la previa
			if (!isValid(playerRow, playerColumn)) {
				playerRow = previousRow;
				playerColumn = previousColumn;
			}

			if (mapa[playerRow][playerColumn] == 'F') {
				mapa[playerRow][playerColumn] = '#';
				flagCounter++;
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

	// array para guardar la información del mapa
	// establecemos el número de filas de forma temporal
	private int mapRows = 10;
	char[][] mapa = new char[mapRows][];

	// posición inicial del personaje en celdas del mapa
	private int playerRow = 1;
	private int playerColumn = 1;
	private int flagCounter = 0;
	
	public GameScreen(Game game) {
		Gdx.app.log(SCREEN_NAME, "Iniciando screen principal del juego");

		this.game = game;

		viewport = new ExtendViewport(GAME_WIDTH, GAME_HEIGHT);

		batch = new SpriteBatch();
		ground = new Texture("suelo.png");
		tree = new Texture("arbol.png");
		mountain = new Texture("montana.png");
		player = new Texture("personaje.png");
		panel = new Texture("panel.png");
		flag = new Texture("bandera.png");

		// cargamos el bitmap de la fuente desde los recursos
		font = new BitmapFont(Gdx.files.internal("arial.fnt"), false);

		// cargamos el fichero en el array en memoria
		try (BufferedReader reader = new BufferedReader(new FileReader("mapa.txt"))) {
			String linea = null;

			int contadorLinea = 0;
			while ((linea = reader.readLine()) != null) {
				if (contadorLinea >= mapRows) {
					// extendemos el array 5 posiciones más
					mapa = Arrays.copyOf(mapa, mapRows + 5);
					mapRows += 5;
				}

				mapa[contadorLinea] = linea.toCharArray();
				contadorLinea++;
			}
		} catch (IOException e) {
			Gdx.app.log(SCREEN_NAME, "No se ha podido cargar el fichero de mapa");
		}

		// registramos el escuchador de teclado
		Gdx.input.setInputProcessor(new KeyboardProcessor());

		Random random = new Random();
		// colocamos 5 banderas al azar
		for (int i = 0; i < 5; i++) {
			int row = random.nextInt(13) + 1;
			int column = random.nextInt(13) + 1;

			mapa[row][column] = 'F';
		}
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
		for (int i = 0; i < mapa.length && mapa[i] != null; i++) {
			for (int j = 0; j < mapa[i].length; j++) {
				// posición actual de dibujado
				// tenemos en cuenta que el eje y está invertido
				// además, la esquina de dibujado de la imagen es la inferior izquierda
				int x = columnToX(j);
				int y = rowToY(i);
				
				switch (mapa[i][j]) {
					case '#': 	batch.draw(ground, x, y, TILE_SIZE, TILE_SIZE);
								break;

					case 'M':	batch.draw(ground, x, y, TILE_SIZE, TILE_SIZE);
								batch.draw(mountain, x, y, TILE_SIZE, TILE_SIZE);
								break;

					case 'A':	batch.draw(ground, x, y, TILE_SIZE, TILE_SIZE);
								batch.draw(tree, x, y, TILE_SIZE, TILE_SIZE);
								break;

					case 'F':	batch.draw(ground, x, y, TILE_SIZE, TILE_SIZE);
								batch.draw(flag, x, y, TILE_SIZE, TILE_SIZE);
								break;

					default: 	break;
				}
			}
		}
		
		// dibujamos el personaje en su posición actual
		batch.draw(player, columnToX(playerColumn), rowToY(playerRow), TILE_SIZE, TILE_SIZE);

		batch.draw(panel, MAP_WIDTH, 0);

		// dibujamos el texto en pantalla
		// en una posición situada a la derecha del mapa
		String counterLabel = String.format("%d flags", flagCounter);
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

	// determina si posición indicada del mapa es válida para el personaje
	public boolean isValid(int row, int column) {
		return mapa[row][column] == '#' || mapa[row][column] == 'F';
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
