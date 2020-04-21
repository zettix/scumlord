package com.zettix.scumlord;


import com.zettix.scumlord.hexgrid.HexGrid;
import com.zettix.scumlord.hexgrid.HexPosition;
import com.zettix.scumlord.tile.*;
import com.zettix.scumlord.tile.enums.*;

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
        Integer[] redArray = {15, 22, 29, 35, 41, 47, 53, 59, 64, 69, 74, 78, 82, 86,
                89, 92, 95, 98, 101, 103, 105, 107, 109, 111, 113, 114, 117, 119,
                121, 123, 125, 127, 129, 131, 133, 135, 137, 139, 141, 143, 145,
                147, 149};
        redLines = Arrays.asList(redArray);
        doShuffule = true;
    }

    public Market getMarket() {
        return market;
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
                System.err.println("Not that one...." + ex.getMessage());
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
            System.err.println("Not that one...." + ex.getMessage());
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
        market = new Market(players.size(), this, doShuffule);
        market.Setup();
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


    private void InitTiles(Player player) {
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
        // Now adjust for changes.  Placing Suburb (score +2), Community Park(income -1, rep +1), score to 3,
        // Heavy Factory (income +1, rep -1), score to 4, income 0.  So need to make funds 15 and score 2.
        PlayerStatChange change = new PlayerStatChange().setFundsChange(1).setPopulationChange(-2);
        player.applyChange(change);
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
                System.err.println("Global Tile " + specialTile + " and " + curiosTile + " share Global Color Effect maybe...");
                if (owningPlayer == placingPlayer && otherColor) {
                    continue;
                }
                SortedSet<SlumColors> targetColors = action.getFilterColors();
                SlumColors color = curiosTile.getColor();
                if (targetColors.contains(color)) { // run that action...
                    PlayerStatChange change = action.getChange();
                    System.err.println("Applying global change " + change);
                    owningPlayer.applyChange(change);
                }
            }
            if (anyTag || otherTag) {
                System.err.println("Global Tile " + specialTile.getName() + " and " + curiosTile.getName() + " share Global Tag Effect maybe...");
                if (owningPlayer == placingPlayer && otherTag) {
                    continue;
                }
                SortedSet<TileTag> targetTags = action.getFilterTags();
                TileTag tileTag = curiosTile.getTileTag();
                if (targetTags.contains(tileTag)) {
                    PlayerStatChange change = action.getChange();
                    System.err.println("Applying global change " + change);
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
            if (otherColor || anyColor || otherTag || anyTag) {
                System.err.println("Adding " + tile.getName() + " to Globals");
               AddGlobalTile(placingPlayer, position);
               for (Player aPlayer : players.values()) {
                   HexGrid board = aPlayer.getBoard();
                   for (HexPosition aPosition : board.getLocations()) {
                       if (placingPlayer == aPlayer && position == aPosition) {
                           continue;  // will be covered later in next for loop.
                       }
                       Tile aTile = board.getTile(aPosition);
                       ApplyColorOrTagEffect(placingPlayer, tile, aTile, aPlayer);
                   }
               }
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

    public void PlaceTile(Player player, Tile tile, String choice) {
        HexPosition position = player.getBoard().getPositionByChoice(choice);
        PlaceTile(player, tile, position);
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
                System.err.println("Instant Action Applied:" + action + " new score:" + newScore);
                //linesCrossed = getLinesCrossed(oldScore, newScore);
                oldScore = newScore;
            }
        }
        // Player is in charge of : Player Adjacents and Player globals.
        PlayerStatChange change = player.addTile(tile, position);
        System.err.println("applying change:" + change);
        player.applyChange(change);
        int newScore = player.getScore();
        System.err.println("Player change Applied:" + change + " new score:" + newScore);
        //linesCrossed = getLinesCrossed(oldScore, newScore);
        newScore = oldScore;
        ApplyGlobalChanges(tile, player, position);
        player.applyStats();
        newScore = player.getScore();
        //linesCrossed = getLinesCrossed(oldScore, newScore);
    }

    public Tile buyTile(Player player, String selection) {
        // TODO(sean): flesh out purchase mechanism.
                String[] startTiles = {"Suburbs",
                "Heavy Factory",
                "Community Park"};
                if (selection.length() == 1) {
                    String choices = "abcdefghijklmnopqrstuvwxyz";
                    String findme = selection.toLowerCase();
                    int idx = choices.indexOf(findme.charAt(0));
                    if (idx < 8) {
                        selection = "Tile:" + idx;
                    } else if (idx > 22) {
                            int startSel = idx - 23;
                            selection = startTiles[startSel] + ":0";
                        } else {
                        int startSel = idx - 10;
                        selection = "Lake:" + startSel;
                    }
                }


        Tile t = null;
        String tileNumberString = selection.substring(selection.indexOf(':') + 1);
        String tileName = selection.substring(0, selection.indexOf(':'));
        int tileNumber = Integer.parseInt(tileNumberString);
        int tax = 0;
        if (tileNumber > 2) {
            tax = 2 * (tileNumber - 2);
        }
        for (String startName : startTiles) {
            if (tileName.equals(startName)) {
                t = market.BuyStarterTile(getTileByName(startName), tileNumber);
                PlayerStatChange change = new PlayerStatChange().setFundsChange(-t.getCost() - tax);
                player.applyChange(change);
                break;
            }
        }
        if (t == null) {
            if (selection.startsWith("Lake:")) {
                PlayerStatChange change = new PlayerStatChange().setFundsChange(-tax);
                player.applyChange(change);
                market.BuyTile(tileNumber); // destroy, drop on the ground, ignore return.
                t = getTileByName("Lake");
            } else if (selection.startsWith("Tile:")) {
                t = market.BuyTile(tileNumber);
                PlayerStatChange change = new PlayerStatChange().setFundsChange(-t.getCost() - tax);
                player.applyChange(change);
            }
        }
        return t;
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
    private File lakeTile;
    private Market market;
    private final List<Integer> redLines;
    public boolean doShuffule;  // for testing.
}
