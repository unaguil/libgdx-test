package com.mygdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainScreen extends ScreenAdapter {
    
    private final Game game;

    private Skin skin;
    private Stage stage;
    private Table table;
    private SpriteBatch batch;
    private Texture background;

    public MainScreen(Game game) {
        this.game = game;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table();
        table.setFillParent(true);
//        table.setDebug(true);

        stage.addActor(table);

        final TextButton startGameButton = new TextButton("Start game", skin);
		table.add(startGameButton).minWidth(200).padTop(100).padBottom(25);

		startGameButton.addListener(new ChangeListener() {

			public void changed (ChangeEvent event, Actor actor) {
                MainScreen.this.game.setScreen(new GameScreen(MainScreen.this.game));
			}
            
		});

        final TextButton exitButton = new TextButton("Exit game", skin);
        table.row();
		table.add(exitButton).minWidth(200);

		exitButton.addListener(new ChangeListener() {

			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
            
		});

        batch = new SpriteBatch();
        background = new Texture("background.jpg");
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();
        batch.draw(background, 0, 0, 800, 600);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
