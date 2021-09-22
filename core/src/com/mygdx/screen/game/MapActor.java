package com.mygdx.screen.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.controller.GameController;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.mygdx.controller.Cell;

// clase que representa una ctor 
public class MapActor extends Actor {

	// tamaño por defecto del mapa
    private static final int TILE_SIZE = 40;
	private static final int MAP_WIDTH = 600;
	private static final int MAP_HEIGHT = 600;

    private GameController gameController;
    
    // referencias a los recursos gráficos cargados
    // como texturas de libGDX
    private TextureRegion ground;
	private TextureRegion tree;
	private TextureRegion mountain;
	private TextureRegion player;
	private TextureRegion flag;

    public MapActor(GameController gameController) {
        this.gameController = gameController;

        // carga de los recursos requeridos para el mapa
        ground = new TextureRegion(new Texture("map/ground.png"));
		tree = new TextureRegion(new Texture("map/tree.png"));
		mountain = new TextureRegion(new Texture("map/mountain.png"));
		flag = new TextureRegion(new Texture("map/flag.png"));
		player = new TextureRegion(new Texture("map/player.png"));

		// establecemos un tamaño por defecto para el actor
		setWidth(MAP_WIDTH);
		setHeight(MAP_HEIGHT);
    }

	// convierte la columna a píxeles en X
	private float columnToX(int column) {
		return column * TILE_SIZE + getX();
	}

	// convierte la fila a píxeles en Y
	private float rowToY(int row) {
		return MAP_HEIGHT - 1 - (TILE_SIZE * (row + 1)) + getY();
	}

    @Override
    public void draw(Batch batch, float parentAlpha) {
		// aqui vamos a dibujar en pantalla cada baldosa
		// desde la esquina superior izquierda
		for (int row = 0; row < gameController.getRows(); row++) {
			for (int column = 0; column < gameController.getColumns(); column++) {
				// posición actual de dibujado
				// tenemos en cuenta que el eje y está invertido
				// además, la esquina de dibujado de la imagen es la inferior izquierda
				float x = columnToX(column);
				float y = rowToY(row);

				batch.draw(ground, 
					x, y, getOriginX(), getOriginY(), 
					TILE_SIZE, TILE_SIZE, getScaleX(), getScaleY(), getRotation()
				);

				TextureRegion content = null;			
				switch (gameController.getCellType(new Cell(row, column))) {
					case MOUNTAIN:	content = mountain;
									break;

					case TREE:		content = tree;
									break;

					case FLAG:		content = flag;
									break;

					default: 		break;
				}

				if (content != null) {
					batch.draw(content, 
						x, y, getOriginX(), getOriginY(), 
						TILE_SIZE, TILE_SIZE, getScaleX(), getScaleY(), getRotation()
					);
				}
			}
		}
		
		// dibujamos el personaje en su posición actual
		float playerXPos = columnToX(gameController.getPlayer().getColumn());
		float playerYPos = rowToY(gameController.getPlayer().getRow());
		batch.draw(player, playerXPos, playerYPos, TILE_SIZE, TILE_SIZE);
    }

	public void dispose() {		
		ground.getTexture().dispose();
		tree.getTexture().dispose();
		mountain.getTexture().dispose();
        flag.getTexture().dispose();
		player.getTexture().dispose();
	}
}
