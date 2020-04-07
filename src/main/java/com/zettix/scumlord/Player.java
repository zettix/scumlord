package com.zettix.scumlord;

import java.lang.management.GarbageCollectorMXBean;
import java.util.HashSet;
import java.util.Set;

public class Player {
    public Player(String name, Game game) {
        this.name = name;
        this.game = game;
        board = new HexGrid();
        income = 0;
        funds = 0;
        reputation = 0;
        score = 0;
        globalPlayerTiles = new HashSet<>();
        adjecentEffectTiles = new HashSet<>();
    }

    public void InitGrid() {
        game.InitTiles(board, this);
    }

    public void applyChange(PlayerStatChange change) {
        income += change.getIncomeChange();
        funds += change.getFundsChange();
        reputation += change.getReputationChange();
        score += change.getPopulationChange();
    }

    public void addTileToGlobals(HexPosition p) {
      globalPlayerTiles.add(p);
    }

    public void addTileToAdjacents(HexPosition p) {
        adjecentEffectTiles.add(p);
    }

    public int getIncome() {return  income;}
    public int getFunds() {return  funds;}
    public int getReputation() {return  reputation;}
    public int getScore() {return  score;}

    private String name = "Anonymous";
    private final HexGrid board;
    private final Game game;
    private int income;
    private int funds;
    private int reputation;
    private int score;
    private final Set<HexPosition> globalPlayerTiles;
    private final Set<HexPosition> adjecentEffectTiles;
}
