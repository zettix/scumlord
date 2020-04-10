package com.zettix.scumlord;


import com.zettix.scumlord.hexgrid.HexGrid;
import com.zettix.scumlord.hexgrid.HexPosition;
import com.zettix.scumlord.tile.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class Game {
    public Game() {
        players = new TreeMap<>();
        seriesTiles = new HashMap<>();
        globalTilesWithEffects = new HashMap<>();
        tileImageMap = new HashMap<>();
        tileNameMap = new HashMap<>();
    }

    public void Load() {
        // Load tiles.
        TileJsonReader reader = new TileJsonReader();
        List<Tile> tiles = reader.Load();
        List<Tile> tilesBySeries = null;
        String outdir = "images/";
        for (Tile tile : tiles) {
            TileSeries series = tile.getSeries();
            String outname = tile.getName().replace(" ", "_");
            String filename = outdir + "Tile_" + series.toString() + "-" + outname + ".png";
            URL url = this.getClass().getClassLoader().getResource(filename);
            File file;
            try {
                file = new File(url.toURI());
                tileImageMap.put(tile, file);
            } catch (URISyntaxException ex) {
                System.err.println("Could not find resource:" + ex.getMessage());
                //return;
            } catch (NullPointerException ex) {
                System.err.println("Not that one...." +
                        "" + ex.getMessage());
            }

            if (seriesTiles.containsKey(series)) {
                tilesBySeries = seriesTiles.get(series);
            } else {
                tilesBySeries = new ArrayList<>();
                seriesTiles.put(series, tilesBySeries);
            }
            tilesBySeries.add(tile);
            tileNameMap.put(tile.getName(), tile);
        }
        File file;
        String filename = outdir + "Tile_Open.png";
        URL url = this.getClass().getClassLoader().getResource(filename);
        try {
            openTile = new File(url.toURI());

        } catch (URISyntaxException ex) {
            System.err.println("Could not find resource:" + ex.getMessage());
            //return;
        } catch (NullPointerException ex) {
            System.err.println("Not that one...." +
                    "" + ex.getMessage());
        }

    }

    public Tile getTileByName(String name) {
        if (tileNameMap.containsKey(name)) {
            return tileNameMap.get(name);
        }
        return null;
    }

    public Set<String> getTileNames() {
        return tileNameMap.keySet();
    }

    public File getTileImageFile(Tile tile) {
        if (tileImageMap.containsKey(tile)) {
            return tileImageMap.get(tile);
        }
        throw new IndexOutOfBoundsException("No such image file:" + tile.toString());
    }

    public File getOpenTile() {
        return openTile;
    }

    public void AddPlayer(Player player) {
        players.put(player.getName(), player);
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

    public void InitTiles(Player player) {
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

        Tile[] placeMe = {suburb, communityPark, heavyFactory};
        HexPosition[] placePos = {
                new HexPosition(0, 0),
                new HexPosition(0, 1),
                new HexPosition(0, 2),
        };

        HexGrid board = player.getBoard();
        for (int idx = 0; idx < 3; idx++) {
            Tile t = placeMe[idx];
            HexPosition hexPosition = placePos[idx];
            PlaceTile(player, t, hexPosition);
        }
    }

    private void ApplyColorOrTagEffect(Player owningPlayer, Tile specialTile, Tile curiosTile) {
        for (TileAction action : specialTile.getActions()) {
            if (action.match(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.ANY)) {
                SortedSet<SlumColors> targetColors = action.getFilterColors();
                SlumColors color = curiosTile.getColor();
                if (targetColors.contains(color)) { // run that action...
                    PlayerStatChange change = action.getChange();
                    owningPlayer.applyChange(change);
                }
            }
            if (action.match(TileEffectType.TAG, TileEffectTime.ONGOING, TileAreaEffect.ANY)) {
                SortedSet<TileTag> targetTags = action.getFilterTags();
                TileTag tileTag = curiosTile.getTileTag();
                if (targetTags.contains(tileTag)) {
                    PlayerStatChange change = action.getChange();
                    owningPlayer.applyChange(change);
                }
            }
        }
    }

    public void ApplyAdjacentChanges(Player player, HexPosition position, Tile tile) {
        Set<HexPosition> effectTiles = player.getAdjacentEffectTile();
        HexGrid board = player.getBoard();
        for (HexPosition effectPosition: effectTiles) {
            Set<HexPosition> sensitivePositions = effectPosition.getNeighbors();
            if (sensitivePositions.contains(position)) {
                // The tile's position overlaps a neighboring site of a tile with adjacent actions.
                Tile effectTile = board.getTile(effectPosition);
                ApplyColorOrTagEffect(player, effectTile, tile);
            }
        }
    }

    public void ApplyGlobalChanges(Player player, HexPosition position, Tile tile) {
        for (Player gobalPlayer : globalTilesWithEffects.keySet()) {
            HexGrid board = player.getBoard();
            Set<HexPosition> positions = globalTilesWithEffects.get(gobalPlayer);
            for (HexPosition ongoingPosition : positions) {
                // Get effects for this ongoing position:
                Tile specialTile = board.getTile(ongoingPosition);
                ApplyColorOrTagEffect(gobalPlayer, specialTile, tile);
            }
        }
    }

    public void PlaceTile(Player player, Tile tile, HexPosition position) {
        HexGrid board = player.getBoard();
        // 1: place tile. 2: apply instants. 3: track ongoing. 4: apply ongoings.
        board.setTile(tile, position);
        //  2.
        List<TileAction> actions = tile.getActions();
        for (TileAction action : actions) {
            if (action.match(TileEffectType.ANY, TileEffectTime.INSTANT, TileAreaEffect.ANY)) {
                // apply instant action.
                PlayerStatChange change = action.getChange();
                player.applyChange(change);
            } else {
                // store ongoing actions.
                if (action.match(TileEffectType.ANY, TileEffectTime.ONGOING, TileAreaEffect.ANY)) {
                    if (action.match(TileEffectType.ANY, TileEffectTime.ONGOING, TileAreaEffect.PLAYER_GLOBAL)) {
                        player.addTileToGlobals(position);
                    }
                    if (action.match(TileEffectType.ANY, TileEffectTime.ONGOING, TileAreaEffect.ADJACENT)) {
                        player.addTileToAdjacents(position);
                    }
                    if (action.match(TileEffectType.ANY, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL)) {
                        Set<HexPosition> hexPositions = null;
                        if (globalTilesWithEffects.containsKey(player)) {
                            hexPositions = globalTilesWithEffects.get(player);
                        } else {
                            hexPositions = new HashSet<>();
                            globalTilesWithEffects.put(player, hexPositions);
                        }
                        hexPositions.add(position);
                    }
                }
            }
        }
        // apply any pending actions.
        ApplyGlobalChanges(player, position, tile);
        // have player apply any pending actions.
        ApplyAdjacentChanges(player, position, tile);
    }

    public List<Tile> getTilesBySeries(TileSeries series) {
        if (seriesTiles.containsKey(series)) {
            return seriesTiles.get(series);
        }
        return new ArrayList<>();
    }

    public void InitAllPlayerTiles() {
        for (Player p : players.values()) {
            InitTiles(p);
        }
    }

    private final TreeMap<String, Player> players;
    private final Map<TileSeries, List<Tile>> seriesTiles;
    private final Map<Player, Set<HexPosition>> globalTilesWithEffects;
    private final Map<Tile, File> tileImageMap;
    private final Map<String, Tile> tileNameMap;
    private File openTile;
}
