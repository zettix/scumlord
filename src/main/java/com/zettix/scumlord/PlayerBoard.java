package com.zettix.scumlord;

public class PlayerBoard {
    public PlayerBoard() {
        grid = new HexGrid();
    }

    private final HexGrid grid;
    private int income = 0;
    private int reputation = 0;
    private int funds = 0;

}
