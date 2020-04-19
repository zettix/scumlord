package com.zettix.scumlord;

import com.zettix.scumlord.hexgrid.HexGrid;
import com.zettix.scumlord.hexgrid.HexPosition;
import com.zettix.scumlord.tile.*;

import java.util.*;

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

    public void applyStats() {
        score += reputation;
        funds += income;
    }

    public void addTileToGlobals(HexPosition p) {
      globalPlayerTiles.add(p);
    }

    public Set<HexPosition> getGlobalTiles() {
        return globalPlayerTiles;
    }

    public void addTileToAdjacents(HexPosition p) {
        adjecentEffectTiles.add(p);
    }


    /**
     * Return tiles that have adjacency rules that affect submitted position.
     * @param p Position of new tile.
     * @return set of tiles that have position rules affecting position p
     */
    private Set<Tile> getAdjacents(HexPosition p) {
        Set<Tile> result = new HashSet<>();
        for (HexPosition position : adjecentEffectTiles) {
            Set<HexPosition> neighbors = position.getNeighbors();
            if (neighbors.contains(p)) {
                result.add(board.getTile(position));
            }
        }
        return result;
    }

    public PlayerStatChange addTile(Tile t, HexPosition p) {
        // immediate effects have taken place.  This is for area/global effects.
        PlayerStatChange change = new PlayerStatChange();
        board.setTile(t, p);
        // First, apply new Tile's Actions.
        for (TileAction action : t.getActions()) {
            Boolean byTag = action.match(TileEffectType.TAG, TileEffectTime.ONGOING, TileAreaEffect.PLAYER_GLOBAL);
            Boolean byColor = action.match(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.PLAYER_GLOBAL);
            Boolean byColorAdjacent =  action.match(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.ADJACENT);
            Boolean byTagAdjacent =  action.match(TileEffectType.TAG, TileEffectTime.ONGOING, TileAreaEffect.ADJACENT);
            if (byColor || byTag) {
                System.err.println("New Tile has player global effects.");
                addTileToGlobals(p);
                for (HexPosition position : board.getLocations()) {
                    Tile someTile = board.getTile(position);
                    if (byColor) {
                        SlumColors color = someTile.getColor();
                        if (action.getFilterColors().contains(color)) { // activated!
                            System.err.println("New Player Global Tile Color Activation between:"
                                    + t.toString() + " and " + someTile.toString());
                            change.addChange(action.getChange());
                        }
                    }
                    if (byTag) {
                        TileTag tag = someTile.getTileTag();
                        if (action.getFilterTags().contains(tag)) { // activated!
                            System.err.println("New Player Global Tile Tag Activation between:"
                                    + t.toString() + " and " + someTile.toString());
                            change.addChange(action.getChange());
                        }
                    }
                }
            }
            if (byColorAdjacent || byTagAdjacent) {
                System.err.println("New Tile " + t.getName() + " has adjacent effects.");
                addTileToAdjacents(p);
                Set<HexPosition> positions = p.getNeighbors();
                for (HexPosition neighborPosition : positions) {
                    Tile neighbor = board.getTile(neighborPosition);
                    if (neighbor != null) {
                        if (byColorAdjacent) {
                            SlumColors color = neighbor.getColor();
                            if (action.getFilterColors().contains(color)) { // activated!
                                System.err.println("Existing Adjacent Tile Color Activation between:"
                                        + t.getName() + " and " + neighbor.getName() + ":" + action.toString());
                                change.addChange(action.getChange());
                            }
                        }
                        if (byTagAdjacent) {
                            TileTag tag = neighbor.getTileTag();
                            if (action.getFilterTags().contains(tag)) { // activated!
                                System.err.println("Existing Adjacent Tile Tag Activation between:"
                                        + t.getName() + " and " + neighbor.getName() + ":" + action.toString());
                                change.addChange(action.getChange());
                            }
                        }
                    }
                }
            }
        }
        System.err.println("CHANGE SO FAR:" + change);

        // Second, apply all Existing tile actions.
        // Adjacent tiles:
        Set<Tile> hotTiles = getAdjacents(p);
        for (Tile hotTile : hotTiles) {
            System.err.println("          Checking adjacency " + hotTile.getName() + " and " + t.getName());
            for (TileAction action : hotTile.getActions()) {
                if (action.match(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.ADJACENT)) {
                    if (action.getFilterColors().contains(t.getColor())) { // serious match here folks...
                        System.err.println("             adding " + action.getChange().toString());
                        change.addChange(action.getChange());
                    }
                }
                if (action.match(TileEffectType.TAG, TileEffectTime.ONGOING, TileAreaEffect.ADJACENT)) {
                    if (action.getFilterTags().contains(t.getTileTag())) { // serious match here folks...
                        System.err.println("             adding " + action.getChange().toString());
                        change.addChange(action.getChange());
                    }
                }
            }
        }
        // player globals:
        for (HexPosition position  : globalPlayerTiles) {
            if (position == p) {
                continue;  // already counted.
            }
            Tile playerGlobalTile = board.getTile(position);
            System.err.println("          Checking player globals " + playerGlobalTile.getName() + " and " + t.getName());
            for (TileAction action : playerGlobalTile.getActions()) {
                if (action.match(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.PLAYER_GLOBAL)) {
                    if (action.getFilterColors().contains(t.getColor())) { // serious match here folks...
                        System.err.println("            g adding " + action.getChange().toString());
                        change.addChange(action.getChange());
                    }
                }
                if (action.match(TileEffectType.TAG, TileEffectTime.ONGOING, TileAreaEffect.PLAYER_GLOBAL)) {
                    if (action.getFilterTags().contains(t.getTileTag())) { // serious match here folks...
                        System.err.println("            g adding " + action.getChange().toString());
                        change.addChange(action.getChange());
                    }
                }
            }
        }
        System.out.println("CHANGE NOW:" + change);

       // change.addChange(ApplyGlobalChanges(t, p));
        System.out.println("CHANGE NOW 2:" + change);
        return change;
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

    public List<Tile> getTilesByTag(TileTag tag) {
        List<Tile> result = new ArrayList<>();
        for (HexPosition position : board.getLocations()) {
            Tile t = board.getTile(position);
            if (t.getTileTag() == tag) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Tile> getTilesByColor(SlumColors color) {
        List<Tile> result = new ArrayList<>();
        for (HexPosition position : board.getLocations()) {
            Tile t = board.getTile(position);
            if (t.getColor() == color) {
                result.add(t);
            }
        }
        return result;
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
