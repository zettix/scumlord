package com.zettix.scumlord;

import com.zettix.scumlord.hexgrid.HexGrid;
import com.zettix.scumlord.hexgrid.HexPosition;

import java.util.HashSet;
import java.util.Set;

public class Player {
    public Player(String name, PlayerStatChange change) {
        this.name = name;
        board = new HexGrid();
        income = change.getIncomeChange();
        funds = change.getFundsChange();
        reputation = change.getReputationChange();
        score = change.getPopulationChange();
        globalPlayerTiles = new HashSet<>();
        adjecentEffectTiles = new HashSet<>();
    }

    public HexGrid getBoard() {
        return board;
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

    Set<HexPosition> getAdjacentEffectTile() {
        return adjecentEffectTiles;
    }

    public String getName() {
        return name;
    }

    public int getIncome() {
        return  income;
    }

    public int getFunds() {
        return  funds;
    }

    public int getReputation() {
        return  reputation;
    }

    public int getScore() {
        return  score;
    }

    private String name = "Anonymous";
    private final HexGrid board;
    private int income;
    private int funds;
    private int reputation;
    private int score;
    private final Set<HexPosition> globalPlayerTiles;
    private final Set<HexPosition> adjecentEffectTiles;
}
