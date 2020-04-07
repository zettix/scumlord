package com.zettix.scumlord;


import com.zettix.scumlord.hexgrid.HexGrid;
import com.zettix.scumlord.hexgrid.HexPosition;
import com.zettix.scumlord.tile.*;

import java.util.*;

public class Game {
    public Game() {
        players = new TreeMap<>();
        seriesTiles = new HashMap<>();
        globalTilesWithEffects = new HashMap<>();
    }

    public void Load() {
        // Load tiles.
        TileJsonReader reader = new TileJsonReader();
        List<Tile> tiles = reader.Load();
        List<Tile> tilesBySeries = null;
        for (Tile tile : tiles) {
            TileSeries series = tile.getSeries();
            if (seriesTiles.containsKey(series)) {
                tilesBySeries = seriesTiles.get(series);
            } else {
                tilesBySeries = new ArrayList<>();
                seriesTiles.put(series, tilesBySeries);
            }
            tilesBySeries.add(tile);
        }

    }


    public Map<String, Integer> getStats() {
       Map<String, Integer> result = new HashMap<>();
       TileSeries[] seriesList = {TileSeries.START, TileSeries.A, TileSeries.B, TileSeries.C};
       for (TileSeries series : seriesList) {
           int sz = 0;
           if (seriesTiles.containsKey(series)) {
               sz = seriesTiles.get(series).size();
           }
           result.put(series.toString(), sz);
       }
       result.put("globalTilesWithEffectsSize:", globalTilesWithEffects.size());
       return result;
    }

    public void InitTiles(HexGrid board, Player player) {
        // place suburb, community park, and heavy factory, one at a time, and change
        // player stats.
        List<Tile> startTiles = seriesTiles.get(TileSeries.START);
        Tile suburb = null;
        Tile heavyFactory = null;
        Tile communityPark = null;
        for (Tile t : startTiles) {
            if (t.getName().equals("Suburbs")) {
                suburb = t;
            }
            if (t.getName().equals("Heavy Factory")) {
                heavyFactory = t;
            }
            if (t.getName().equals("Community Park")) {
                communityPark = t;
            }
        }

        if (suburb == null || heavyFactory == null || communityPark == null) {
            throw new InternalError("Cannot find all starter tiles.");
        }

        Tile[] placeMe = {suburb, heavyFactory, communityPark};
        HexPosition[] placePos = {
                new HexPosition(0, 0),
                new HexPosition(0, 1),
                new HexPosition(0, 2),
        };

        for (int idx = 0; idx < 3; idx++) {
            Tile t = placeMe[idx];
            HexPosition hexPosition = placePos[idx];
            // 1: place tile. 2: apply instants. 3: track ongoing. 4: apply ongoings.
            board.setTile(t, hexPosition);
            //  2.
            List<TileAction> actions = t.getActions();
            for (TileAction action : actions) {
                if (action.match(TileEffectType.ANY, TileEffectTime.INSTANT, TileAreaEffect.ANY)) {
                    // apply instant action.
                    PlayerStatChange change = action.getChange();
                    player.applyChange(change);
                } else {
                    // store ongoing actions.
                    if (action.match(TileEffectType.ANY, TileEffectTime.ONGOING, TileAreaEffect.ANY)) {
                        if (action.match(TileEffectType.ANY, TileEffectTime.ONGOING, TileAreaEffect.PLAYER_GLOBAL)) {
                            player.addTileToGlobals(hexPosition);
                        }
                        if (action.match(TileEffectType.ANY, TileEffectTime.ONGOING, TileAreaEffect.ADJACENT)) {
                            player.addTileToAdjacents(hexPosition);
                        }
                        if (action.match(TileEffectType.ANY, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL)) {
                            Set<HexPosition> hexPositions = null;
                            if (globalTilesWithEffects.containsKey(player)) {
                                hexPositions = globalTilesWithEffects.get(player);
                            } else {
                                hexPositions = new HashSet<>();
                                globalTilesWithEffects.put(player, hexPositions);
                            }
                            hexPositions.add(hexPosition);
                        }
                    }
                }
            }
            // apply any pending actions.
            ApplyGlobalChanges(player, hexPosition);
            // have player apply any pending actions.


        }
    }

    public void ApplyGlobalChanges(Player player, HexPosition position) {

    }

    public void InitAllPlayerTiles() {

        for (Player p : players.values()) {
            p.InitGrid();
        }
    }

    private final TreeMap<String, Player> players;
    private final Map<TileSeries, List<Tile>> seriesTiles;
    private final Map<Player, Set<HexPosition>> globalTilesWithEffects;
}
