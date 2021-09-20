package com.mygdx.screen.gamemap;

// contiene información sobre el jugador
public class Player {
    
    // posición del jugador en el mapa
    private int row;
    private int column;

    public Player() {
        this.row = 1;
        this.column = 1;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }
}
