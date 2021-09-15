package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		Gdx.app.log("Test App", "App creada");
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		//Gdx.app.log("Test App", "renderizando...");
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		Gdx.app.log("Test App", "Deteniendo el app");
		batch.dispose();
		img.dispose();
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log("Test App", String.format("Tamaño pantalla: %d x %d", width, height));
	}

	@Override
	public void pause() {
		Gdx.app.log("Test App", "App pausada");
	}

	@Override
	public void resume() {
		Gdx.app.log("Test App", "App restaurada");
	}
}
