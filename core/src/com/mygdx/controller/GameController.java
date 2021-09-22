package com.mygdx.controller;

import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.EnumSet;
import java.io.FileReader;

import java.util.Random;

/**
 * Clase que representa el mapa del juego con la información
 * del objeto contenido en cada casilla.
 */
public class GameController {

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
    private Cell player = new Cell(0, 0);

    // contador de banderas recogidas en el mapa
    private int flagCounter;
    
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

    // obtiene la posición del jugador
    public Cell getPlayer() {
        return player;
    }

    // obtiene el tipo de objeto que hay en la celda 
    public CellType getCellType(Cell cell) {
        return mapData[cell.getRow()][cell.getColumn()];
    }

    // establece el valor de la celda
    private void setCellType(Cell cell, CellType cellType) {
        mapData[cell.getRow()][cell.getColumn()] = cellType;
    }

    // determina si la posición indica es válida
	private boolean isValid(Cell cell) {
        Set<CellType> validCells = EnumSet.of(CellType.GROUND, CellType.FLAG);
        return validCells.contains(getCellType(cell));
	}

    // elimina las banderas existentes en el mapa
    private void clearMap() {
        for (int row = 0; row < getRows(); row++) {
            for (int column = 0; column < getColumns(); column++) {
                Cell cell = new Cell(row, column);
                if (getCellType(cell) == CellType.FLAG) {
                    setCellType(cell, CellType.GROUND);
                }
            }
        }
    }

    // obtiene una celda aleatoria del mapa
    public Cell getRandomCell() {
        return new Cell(
            random.nextInt(getRows()), 
            random.nextInt(getColumns())
        );
    }

    // añade n banderas aleatorias en el mapa
    private void addRandomFlags(int numFlags) {
        // colocamos 5 banderas al azar
        // en posiciones donde no haya nada
        while (numFlags > 0) {
            Cell randomCell = getRandomCell();

            // si la celda es de suelo se puede colocar la bandera
            // y se indica que quedan menos banderas por colocar
            if (getCellType(randomCell) == CellType.GROUND) {
                setCellType(randomCell, CellType.FLAG);
                numFlags--;
            }
        }
    }

    // situa aleatoriamente al jugador en el mapa
    // en una celda que está vacía
    private void initPlayer() {
        boolean playerInit = false;
        while (!playerInit) {
            Cell randomCell = getRandomCell();

            if (getCellType(randomCell) == CellType.GROUND) {
                player = randomCell;
                playerInit = true;
            }
        }
    }

    // reinicia el mapa de juego recolocando 5 banderas
    // y la posición del jugador aleatoria
    public void restartMap() {
        clearMap();
        addRandomFlags(5);
        initPlayer();
        flagCounter = 0;
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
        Cell newPosition = new Cell(player.getRow(), player.getColumn());

        switch (d) {
            case DOWN:  newPosition.setRow(newPosition.getRow() + 1);
                        break;
            
            case UP:	newPosition.setRow(newPosition.getRow() - 1);;
                        break;

            case RIGHT:	newPosition.setColumn(newPosition.getColumn() + 1);
                        break;

            case LEFT:	newPosition.setColumn(newPosition.getColumn() - 1);
                        break;

            default:	break;
        }

        // comprobar si la nueva localización es válida
        // y establecer el jugador en dicha posición
        if (isValid(newPosition)) {
            player.setRow(newPosition.getRow());
            player.setColumn(newPosition.getColumn());
        }

        // comprobar si en la nueva posición del jugador hay una bandera
        if (getCellType(player) == CellType.FLAG) {
            // si hay bandera, eliminarla y aumentar el contador
            setCellType(player, CellType.GROUND);
            flagCounter++;
        }
    }
}
