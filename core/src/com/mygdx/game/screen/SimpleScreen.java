package com.mygdx.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.ScreenAdapter;

public class SimpleScreen extends ScreenAdapter {

	private Camera camera;
	private ScreenViewport viewport;
	private Texture mountain;
	private SpriteBatch batch;
	
	public SimpleScreen() {		
		camera = new OrthographicCamera();		
		viewport = new ScreenViewport(camera);
		
		mountain = new Texture("montana.png");
		batch = new SpriteBatch();
	}

	@Override
	public void render(float delta) {
		// borrado de pantalla para empezar a dibujar el fotograma
		ScreenUtils.clear(0, 0, 0, 1);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin(); // requerido para empezar a dibujar
		batch.draw(mountain, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		mountain.dispose();
	}

	@Override
	public void resize(int width, int height) {
        viewport.update(width, height);
		camera.position.set(width / 2f, height / 2f, 0);
    }
}
