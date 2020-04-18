package com.zettix.scumlord;

import com.zettix.scumlord.tile.Tile;
import com.zettix.scumlord.tile.TileSeries;

import java.util.*;

public class Market {
    private Map<TileSeries, List<Tile>> market;
    private Map<Tile, Integer> startTileCount;
    private final int playerCount;
    private final Game game;
    private final int MARKET_SIZE = 7;
    private List<Tile> counterTop;
    private final boolean doShuffle;

    private Market() {
        playerCount = 0;
        game = null;
        doShuffle = false;
    }

    public Market(int playerCount, Game game, boolean doShuffle) {
        this.game = game;
        this.playerCount = playerCount;
        market = new HashMap<>();
        counterTop = new ArrayList<>();
        startTileCount = new HashMap<>();
        this.doShuffle = doShuffle;
    }

    private void LoadCounterBySeries(TileSeries series) {
        while (market.get(series).size() > 0) {
            Tile t = market.get(series).get(0);
            market.get(series).remove(0);
            counterTop.add(t);
            if (counterTop.size() == MARKET_SIZE) {
                break;
            }
        }
    }

    private void LoadCounterTop() {
        TileSeries[] loadOrder = {TileSeries.A, TileSeries.B, TileSeries.C};
        for (TileSeries series : loadOrder) {
            if (counterTop.size() < MARKET_SIZE) {
                LoadCounterBySeries(series);
            }
        }
    }

    public void Setup() {
        Load();
        if (doShuffle) {
            Shuffle();
        }
        TruncateTiles();
        LoadCounterTop();
    }

    private void TruncateTiles() {
        // TODO(sean): move to resource.
        Map<Integer, Map<TileSeries, Integer>> tileCountMap = new HashMap<>();
        Map<TileSeries, Integer> map1 = new HashMap<>();
        map1.put(TileSeries.A, 14);
        map1.put(TileSeries.B, 12);
        map1.put(TileSeries.C, 14);
        Map<TileSeries, Integer> map2 = new HashMap<>();
        map2.put(TileSeries.A, 14);
        map2.put(TileSeries.B, 12);
        map2.put(TileSeries.C, 14);
        Map<TileSeries, Integer> map3 = new HashMap<>();
        map3.put(TileSeries.A, 14);
        map3.put(TileSeries.B, 12);
        map3.put(TileSeries.C, 14);
        Map<TileSeries, Integer> map4 = new HashMap<>();
        map4.put(TileSeries.A, 14);
        map4.put(TileSeries.B, 12);
        map4.put(TileSeries.C, 14);
        tileCountMap.put(1, map1);
        tileCountMap.put(2, map2);
        tileCountMap.put(3, map3);
        tileCountMap.put(4, map4);
        for (TileSeries tileSeries : market.keySet()) {
            int count =  tileCountMap.get(playerCount).get(tileSeries);
            market.put(tileSeries, new ArrayList<>(market.get(tileSeries).subList(0, count)));
        }
    }


    public void Shuffle() {
        for (TileSeries tileSeries : market.keySet()) {
            Collections.shuffle(market.get(tileSeries));
        }
    }


    public void Load() {
        // TODO(sean): move to resource.
        Map<Integer, Integer> playerToStarterTileCount = new HashMap<>();
        playerToStarterTileCount.put(1, 3);
        playerToStarterTileCount.put(2, 4);
        playerToStarterTileCount.put(3, 5);
        playerToStarterTileCount.put(4, 6);

        List<Tile> startTiles = game.getTilesBySeries(TileSeries.START);
        //List<Tile> myStartTiles = new ArrayList<>();
        for (Tile t: startTiles) {
            //myStartTiles.add(t);
            startTileCount.put(t, playerToStarterTileCount.get(playerCount));
        }

        // Add n copies of tiles to lists before shuffle.
        for (TileSeries tileSeries : TileSeries.values()) {
            if (tileSeries ==  TileSeries.START) {
                continue;
            }
            List<Tile> gameTiles = game.getTilesBySeries(tileSeries);
            List<Tile> myTiles = new ArrayList<>();
            for (Tile t : gameTiles) {
                for (int i = 0; i < t.getCount(); i++) {
                    myTiles.add(t);
                }
            }
            market.put(tileSeries, myTiles);
        }
    }

    public Tile BuyStarterTile(Tile t) {
        Tile aStarterTile = null;
        for (Tile starterTile : startTileCount.keySet()) {
            if (t == starterTile) {
                int count = startTileCount.get(t);
                count--;
                startTileCount.put(t, count);
                aStarterTile = t;
                break;
            }
        }
        return aStarterTile;
    }

    public Tile BuyTile(int index) {
        Tile result = counterTop.get(index);
        counterTop.remove(index);
        LoadCounterTop();
        return result;
    }

    public List<Tile> getCounterTop() {
        return counterTop;
    }

    public Map<Tile, Integer> getStartTileCounts() {
        Map<Tile, Integer> result = new HashMap<>();
        for (Tile t : startTileCount.keySet()) {
            result.put(t, startTileCount.get(t));
        }
        return result;
    }

    public Map<TileSeries, Integer> getSeriesTileCounts() {
        Map<TileSeries, Integer> result = new HashMap<>();
        for (TileSeries s : market.keySet()) {
            result.put(s, market.get(s).size());
        }
        return result;
    }
}
