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
        market = null;
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

    private void AddGlobalTile(Player player, HexPosition position) {
        Set<HexPosition> positions = null;
        if (globalTilesWithEffects.containsKey(player)) {
            positions = globalTilesWithEffects.get(player);
        } else {
            positions = new HashSet<>();
        }
        positions.add(position);
        globalTilesWithEffects.put(player, positions);
    }

    public void Setup() {
        // players added.  Set up initial player boards and market.
        InitAllPlayerTiles();
        market = new Market(players.size(), this);
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

    private void filterByName(Tile t, String s, Map<String, Tile> map) {
    }

    public void InitTiles(Player player) {
        // place suburb, community park, and heavy factory, one at a time, and change
        // player stats.
        List<Tile> startTiles = seriesTiles.get(TileSeries.START);
        Map<String, Tile> myTiles  = new HashMap<>();
        String[] placeMe = {"Suburbs", "Community Park", "Heavy Factory"};
        for (Tile t : startTiles) {
            for (String s : placeMe) {
                if (t.getName().equals(s)) {
                    myTiles.put(s, t);
                }
            }
        }
        if (myTiles.size() != 3) {
            throw new InternalError("Cannot find all starter tiles.");
        }
        HexPosition[] placePos = {
                new HexPosition(0, 0),
                new HexPosition(0, 1),
                new HexPosition(0, 2),
        };

        for (int idx = 0; idx < 3; idx++) {
            Tile t = myTiles.get(placeMe[idx]);
            HexPosition hexPosition = placePos[idx];
            PlaceTile(player, t, hexPosition);
        }
    }

    private void ApplyColorOrTagEffect(Player owningPlayer, Tile specialTile, Tile curiosTile, Player placingPlayer) {
        // if placing, do for all existing tiles everywhere, then do 1by1.
        // This is run for every global tile in game inventory.
        for (TileAction action : specialTile.getActions()) {
            Boolean otherTag = action.match(TileEffectType.TAG, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL_OTHER);
            Boolean otherColor = action.match(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL_OTHER);
            Boolean anyTag = action.match(TileEffectType.TAG, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL);
            Boolean anyColor = action.match(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL);
            if (anyColor || otherColor) {
                if (owningPlayer == placingPlayer && otherColor) {
                    continue;
                }
                SortedSet<SlumColors> targetColors = action.getFilterColors();
                SlumColors color = curiosTile.getColor();
                if (targetColors.contains(color)) { // run that action...
                    PlayerStatChange change = action.getChange();
                    owningPlayer.applyChange(change);
                }
            }
            if (anyTag || otherTag) {
                if (owningPlayer == placingPlayer && otherTag) {
                    continue;
                }
                SortedSet<TileTag> targetTags = action.getFilterTags();
                TileTag tileTag = curiosTile.getTileTag();
                if (targetTags.contains(tileTag)) {
                    PlayerStatChange change = action.getChange();
                    owningPlayer.applyChange(change);
                }
            }
        }
    }

    public void ApplyGlobalChanges(Tile tile, Player placingPlayer, HexPosition position) {
        for (TileAction action : tile.getActions()) {
            Boolean otherTag = action.match(TileEffectType.TAG, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL_OTHER);
            Boolean otherColor = action.match(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL_OTHER);
            Boolean anyTag = action.match(TileEffectType.TAG, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL);
            Boolean anyColor = action.match(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL);
            if (otherColor || anyColor || otherColor || anyTag) {
               AddGlobalTile(placingPlayer, position);
               // TODO: apply global change to all exisiting tiles.
            }
        }
        for (Player globalPlayer : globalTilesWithEffects.keySet()) {
            HexGrid board = globalPlayer.getBoard();
            Set<HexPosition> positions = globalTilesWithEffects.get(globalPlayer);
            for (HexPosition ongoingPosition : positions) {
                Tile specialTile = board.getTile(ongoingPosition);
                ApplyColorOrTagEffect(globalPlayer, specialTile, tile, placingPlayer);
            }
        }
    }

    public void PlaceTile(Player player, Tile tile, HexPosition position) {
        HexGrid board = player.getBoard();
        // 1: apply instants.
        // 2 place tile.
        //   2.1 Apply adjacents.
        //   2.2 Apply player globals
        //   2.3 Apply game globals
        int linesCrossed = 0;
        int oldScore = player.getScore();
        List<TileAction> actions = tile.getActions();
        for (TileAction action : actions) {
            if (action.match(TileEffectType.ANY, TileEffectTime.INSTANT, TileAreaEffect.ANY)) {
                // apply instant action.
                PlayerStatChange change = action.getChange();
                oldScore = player.getScore();
                player.applyChange(change);
                int newScore = player.getScore();
                //linesCrossed = getLinesCrossed(oldScore, newScore);
                oldScore = newScore;
            }
        }
        // Player is in charge of : Player Adjacentsand Player globals.
        PlayerStatChange change = player.addTile(tile, position);
        player.applyChange(change);
        int newScore = player.getScore();
        //linesCrossed = getLinesCrossed(oldScore, newScore);
        newScore = oldScore;
        ApplyGlobalChanges(tile, player, position);
        newScore = player.getScore();
        //linesCrossed = getLinesCrossed(oldScore, newScore);
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
    private Market market;
}
