package com.mygdx.screen.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.controller.GameController;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.mygdx.controller.Cell;

// clase que representa una ctor 
public class MapActor extends Actor {

    private static final int TILE_SIZE = 40;
	private static final int MAP_HEIGHT = TILE_SIZE * 15;

    private GameController gameController;
    
    // referencias a los recursos gráficos cargados
    // como texturas de libGDX
    private Texture ground;
	private Texture tree;
	private Texture mountain;
	private Texture player;
	private Texture flag;

    public MapActor(GameController gameController) {
        this.gameController = gameController;

        // carga de los recursos requeridos para el mapa
        ground = new Texture("map/ground.png");
		tree = new Texture("map/tree.png");
		mountain = new Texture("map/mountain.png");
		flag = new Texture("map/flag.png");
		player = new Texture("map/player.png");
    }

    
	// método de utilidad para convertir la columna indica a píxeles en la textura
	public int rowToY(int row) {
		return MAP_HEIGHT - 1 - (TILE_SIZE * (row + 1));
	}

	// convierte la columna a píxeles
	public int columnToX(int column) {
		return column * TILE_SIZE;
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
    }

	public void dispose() {		
		ground.dispose();
		tree.dispose();
		mountain.dispose();
        flag.dispose();
		player.dispose();
	}
}
