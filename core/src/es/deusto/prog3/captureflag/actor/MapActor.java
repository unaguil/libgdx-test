package es.deusto.prog3.captureflag.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;

import es.deusto.prog3.captureflag.controller.Cell;
import es.deusto.prog3.captureflag.controller.GameController;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Batch;

// clase que representa una ctor 
public class MapActor extends Actor {

	// tamaño por defecto del mapa
    private static final int TILE_SIZE = 40;
	private static final int MAP_WIDTH = 600;
	private static final int MAP_HEIGHT = 600;

	// tamaño original de las texturas como recurso
	private static final int TEXTURE_SIZE = 16;

    private GameController gameController;
    
    // referencias a los recursos gráficos cargados
    // como texturas de libGDX

	private Texture tileSet;
    private TextureRegion ground;
	private TextureRegion tree;
	private TextureRegion mountain;
	private TextureRegion player;
	private TextureRegion flag;

    public MapActor(GameController gameController) {
        this.gameController = gameController;

        // carga de los recursos requeridos para el mapa
		tileSet = new Texture("gamescreen/map/map-tileset.png");
        ground = getRegion(tileSet, 1, 3);
		tree = getRegion(tileSet, 1, 6);
		mountain = getRegion(tileSet, 2, 6);
		flag = getRegion(tileSet, 7, 6);
		player = getRegion(tileSet, 15, 6);

		// establecemos un tamaño por defecto para el actor
		setWidth(MAP_WIDTH);
		setHeight(MAP_HEIGHT);
    }

	// método para obtener la referencia a la región de la textura en
	// el tileset de imágenes para el mapa
	// el número de fila y columna corresponden con la posición en el tileset
	private TextureRegion getRegion(Texture texture, int row, int column) {
		return new TextureRegion(texture, TEXTURE_SIZE * (column - 1), TEXTURE_SIZE * (row - 1), TEXTURE_SIZE, TEXTURE_SIZE);
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
