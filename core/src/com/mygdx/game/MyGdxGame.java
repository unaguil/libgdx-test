package com.mygdx.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;

public class MyGdxGame extends ApplicationAdapter {

	private static final String NOMBRE_APP = "Test app";
	private static final int ANCHO_MAPA = 25;
	private static final int ALTO_MAPA = 25;
	private static final int TAM_BALDOSA = 16;

	SpriteBatch batch;
	Texture suelo;
	Texture arbol;
	Texture montana;

	char[][] mapa = new char[ANCHO_MAPA][ALTO_MAPA];
	
	@Override
	public void create() {
		Gdx.app.log(NOMBRE_APP, "App creada");
		batch = new SpriteBatch();
		suelo = new Texture("suelo.png");
		arbol = new Texture("arbol.png");
		montana = new Texture("montana.png");

		// cargamos el fichero en el array en memoria
		try (BufferedReader reader = new BufferedReader(new FileReader("mapa.txt"))) {
			String linea = null;

			int contadorLinea = 0;
			while ((linea = reader.readLine()) != null) {
				mapa[contadorLinea] = linea.toCharArray();
				contadorLinea++;
			}
		} catch (IOException e) {
			Gdx.app.log(NOMBRE_APP, "No se ha podido cargar el fichero de mapa");
		}
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();

		// aqui vamos a dibujar en pantalla cada baldosa
		// desde la esquina superior izquierda

		for (int i = 0; i < ANCHO_MAPA; i++) {
			for (int j = 0; j < ALTO_MAPA; j++) {
				// posición actual de dibujado
				// tenemos en cuenta que el eje y está invertido
				// además, la esquina de dibujado de la imagen es la inferior izquierda
				int x = j * TAM_BALDOSA; 
				int y = Gdx.graphics.getHeight() - 1 - (TAM_BALDOSA * (i + 1)); 
				
				switch (mapa[i][j]) {
					case '#': 	batch.draw(suelo, x, y);
								break;

					case 'M':	batch.draw(suelo, x, y);
								batch.draw(montana, x, y);
								break;

					case 'A':	batch.draw(suelo, x, y);
								batch.draw(arbol, x, y);
								break;

					default: 	break;
				}
			}
		}
		
		batch.end();
	}
	
	@Override
	public void dispose() {
		Gdx.app.log(NOMBRE_APP, "Deteniendo el app");
		batch.dispose();
		suelo.dispose();
		arbol.dispose();
		montana.dispose();
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log(NOMBRE_APP, String.format("Tamaño pantalla: %d x %d", width, height));
	}

	@Override
	public void pause() {
		Gdx.app.log(NOMBRE_APP, "App pausada");
	}

	@Override
	public void resume() {
		Gdx.app.log("Test App", "App restaurada");
	}
}
