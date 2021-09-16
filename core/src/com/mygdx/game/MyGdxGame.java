package com.mygdx.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class MyGdxGame extends ApplicationAdapter {

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

			return false;
		}
	}

	private static final String APP_NAME = "Test app";
	private static final int MAP_WIDTH = 25;
	private static final int MAP_HEIGHT = 25;
	private static final int TILE_SIZE = 16;

	private Stage stage;
	private Viewport viewport;
	private Camera camera;
	private SpriteBatch batch;
	private Texture ground;
	private Texture tree;
	private Texture mountain;
	private Texture player;

	private BitmapFont font;

	// array para guardar la información del mapa
	char[][] mapa = new char[MAP_WIDTH][MAP_HEIGHT];

	// posición inicial del personaje en celdas del mapa
	private int playerRow = 1;
	private int playerColumn = 1;
	private int flagCounter = 0;
	
	@Override
	public void create() {
		Gdx.app.log(APP_NAME, "App creada");
		batch = new SpriteBatch();
		ground = new Texture("suelo.png");
		tree = new Texture("arbol.png");
		mountain = new Texture("montana.png");
		player = new Texture("personaje.png");

		// cargamos el bitmap de la fuente desde los recursos
		font = new BitmapFont(Gdx.files.internal("arial.fnt"), false);

		// cargamos el fichero en el array en memoria
		try (BufferedReader reader = new BufferedReader(new FileReader("mapa.txt"))) {
			String linea = null;

			int contadorLinea = 0;
			while ((linea = reader.readLine()) != null) {
				mapa[contadorLinea] = linea.toCharArray();
				contadorLinea++;
			}
		} catch (IOException e) {
			Gdx.app.log(APP_NAME, "No se ha podido cargar el fichero de mapa");
		}

		// registramos el escuchador de teclado
		Gdx.input.setInputProcessor(new KeyboardProcessor());

	}

	@Override
	public void render() {
		// borrado de pantalla para empezar a dibujar el fotograma
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin(); // requerido para empezar a dibujar

		// aqui vamos a dibujar en pantalla cada baldosa
		// desde la esquina superior izquierda
		for (int i = 0; i < MAP_HEIGHT; i++) {
			for (int j = 0; j < MAP_WIDTH; j++) {
				// posición actual de dibujado
				// tenemos en cuenta que el eje y está invertido
				// además, la esquina de dibujado de la imagen es la inferior izquierda
				int x = columnToX(j);
				int y = rowToY(i);
				
				switch (mapa[i][j]) {
					case '#': 	batch.draw(ground, x, y);
								break;

					case 'M':	batch.draw(ground, x, y);
								batch.draw(mountain, x, y);
								break;

					case 'A':	batch.draw(ground, x, y);
								batch.draw(tree, x, y);
								break;

					default: 	break;
				}
			}
		}
		
		// dibujamos el personaje en su posición actual
		batch.draw(player, columnToX(playerColumn), rowToY(playerRow));

		// dibujamos el texto en pantalla
		// en una posición situada a la derecha del mapa
		String counterLabel = String.format("%d bandera(s)", flagCounter);
		font.draw(batch, counterLabel, MAP_WIDTH * TILE_SIZE + 20, Gdx.graphics.getHeight() - 20);
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
		return mapa[row][column] == '#';
	}
	
	@Override
	public void dispose() {
		Gdx.app.log(APP_NAME, "Deteniendo el app");
		batch.dispose();
		
		ground.dispose();
		tree.dispose();
		mountain.dispose();
		player.dispose();
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log(APP_NAME, String.format("Tamaño pantalla: %d x %d", width, height));
	}

	@Override
	public void pause() {
		Gdx.app.log(APP_NAME, "App pausada");
	}

	@Override
	public void resume() {
		Gdx.app.log(APP_NAME, "App restaurada");
	}
}
