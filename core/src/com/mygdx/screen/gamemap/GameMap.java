package com.mygdx.screen.gamemap;

import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.io.FileReader;

import java.util.Random;

/**
 * Clase que representa el mapa del juego con la información
 * del objeto contenido en cada casilla.
 */
public class GameMap {

    // enumerado para representar el contenido de cada celda
    public enum CellType {
        GROUND('#'),
        MOUNTAIN('M'),
        TREE('T'),
        FLAG('F');

        // caracter usado en la representación de fichero
        private char c;

        CellType(char c) {
            this.c = c;
        }

        public static CellType fromChar(char c) {
            for (CellType cell : CellType.values()) {
                if (cell.c == c) {
                    return cell;
                }
            }

            throw new IllegalArgumentException(String.format("No constant with text '%c' found", c));
        }
    }
    
    // array para guardar la información del mapa
	// establecemos el número de filas de forma temporal
    // número de filas del mapa, temporalmente son 10
    private int mapRows = 10;
	private CellType[][] mapData = new CellType[mapRows][];

    // información sobre el jugador en el mapa de juego
    private Player player = new Player();

    // contador de banderas recogidas en el mapa
    private int flagCounter = 0;
    
    // random para generar la posición de las banderas
    private Random random = new Random();

    // método para cargar la información del mapa de juego desde
    // fichero
    public void loadFile(Path mapFile) throws IOException {
        // cargamos el fichero en el array en memoria
		try (BufferedReader reader = new BufferedReader(new FileReader(mapFile.toFile()))) {
			String line = null;

			int lineCounter = 0;
			while ((line = reader.readLine()) != null) {
                // comprobamos si hay sitio en el array
				if (lineCounter >= mapRows) {
					// extendemos el array 5 posiciones más
					mapData = Arrays.copyOf(mapData, mapRows + 5);
					mapRows += 5;
				}

                // cada línea es convertida a un array de enumerados
                // antes de asignarla a la fila actual del array del mapa
				char[] lineChars = line.toCharArray();
                mapData[lineCounter] = new CellType[lineChars.length];
                for (int i = 0; i < lineChars.length; i++) {
                    mapData[lineCounter][i] = CellType.fromChar(lineChars[i]);
                }
				lineCounter++;
			}

            // guardamos el número de filas leídas desde fichero
            mapRows = lineCounter;
		}
    }

    // obtiene el número de filas del mapa
    public int getRows() {
        return mapRows;
    }

    // obtiene el número de columnas del mapa
    public int getColumns() {
        return mapData[0].length;
    }

    // obtiene el tipo de objeto que hay en la celda 
    public CellType getCellType(int row, int column) {
        return mapData[row][column];
    }

    // determina si la posición del jugador es válida
	private boolean isValid() {
		return mapData[player.getRow()][player.getColumn()] == CellType.GROUND 
            || mapData[player.getRow()][player.getColumn()] == CellType.FLAG;
	}

    // añade n banderas aleatorias en el mapa
    public void addRandomFlags() {
        // colocamos 5 banderas al azar
        for (int i = 0; i < 5; i++) {
            int row = random.nextInt(13) + 1;
            int column = random.nextInt(13) + 1;

            mapData[row][column] = CellType.FLAG;
        }
    }

    // obtiene la información sobre el jugador en el mapa
    public Player getPlayer() {
        return player;
    }

    // obtiene información sobre el número de banderas recogidas
    public int getCapturedFlags() {
        return flagCounter;
    }

    // enumerado para representar la dirección de movimiento
    // del personaje
    public enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }

    // intenta mover al jugador en la dirección indicada
    public void move(Direction d) {        
        int newRow = player.getRow();
        int newColumn = player.getColumn();

        switch (d) {
            case DOWN:  newRow++;
                        break;
            
            case UP:	newRow--;
                        break;

            case RIGHT:	newColumn++;
                        break;

            case LEFT:	newColumn--;
                        break;

            default:	break;
        }

        // comprobar si la nueva localización es válida
        // y establecer el jugador en dicha posición
        if (isValid()) {
            player.setPosition(newRow, newColumn);
        }

        if (mapData[player.getRow()][player.getColumn()] == CellType.FLAG) {
            mapData[player.getRow()][player.getColumn()] = CellType.GROUND;
            flagCounter++;
        }
    }
}
