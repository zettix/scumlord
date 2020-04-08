package com.zettix.scumlord;

import com.zettix.scumlord.hexgrid.HexGrid;
import com.zettix.scumlord.hexgrid.HexPosition;
import com.zettix.scumlord.tile.Tile;
import com.zettix.scumlord.tile.TileSeries;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GameTest {

    @Before
    public void startUp() {
        game = new Game();
    }

    @Test
    public void load() {
        game.Load();
        Map<String, Integer> resultmap = game.getStats();
        int startSize = resultmap.get(TileSeries.START.toString());
        assertEquals(startSize, 3);
    }

    @Test
    public void initTiles() {
        game.Load();
        PlayerStatChange change = new PlayerStatChange().setFundsChange(15);
        Player player = new Player("Test Player", change);
        game.AddPlayer(player);
        game.InitTiles(player);
        assertEquals(player.getFunds(), 15);
        assertEquals(player.getIncome(), 0);
        assertEquals(player.getReputation(), 1);
        assertEquals(player.getScore(), 2);
    }

    @Test
    public void initAllPlayerTiles() {
        game.Load();
        PlayerStatChange change = new PlayerStatChange().setFundsChange(15);
        Player player = new Player("Test Player", change);
        game.AddPlayer(player);
        Player player2 = new Player("Test Player the Second", change);
        game.AddPlayer(player2);

        game.InitAllPlayerTiles();
        assertEquals(player.getFunds(), 15);
        assertEquals(player.getIncome(), 0);
        assertEquals(player.getReputation(), 1);
        assertEquals(player.getScore(), 2);
        assertEquals(player2.getFunds(), 15);
        assertEquals(player2.getIncome(), 0);
        assertEquals(player2.getReputation(), 1);
        assertEquals(player2.getScore(), 2);
    }

    @Test
    public void testAddFive() {
        game.Load();
        PlayerStatChange change = new PlayerStatChange().setFundsChange(15);
        Player player = new Player("Test Player", change);
        game.AddPlayer(player);
        game.InitAllPlayerTiles();
        String[] tilesToAdd = {
                "Domestic Airport",
                "Boutique",
                "Shipping Center",
                "Elementary School",
                "University"
        };
        Map<String, Tile> targetTiles = new HashMap<>();
        for (Tile tile : game.getTilesBySeries(TileSeries.A)) {
            for (String name : tilesToAdd) {
                if (tile.getName().equals(name)) {
                    targetTiles.put(name, tile);
                }
            }
        }

        HexPosition[] positionsToAdd = {
                new HexPosition(0, 4),
                new HexPosition(0, 5),
                new HexPosition(0, 6),
                new HexPosition(0, 7),
                new HexPosition(0, 8),
        };
        int[] expectedScore = {2, 3, 4, 6, 9, 12};
        int[] expectedIncome = {0, 2, 3, 3, 3, 5};
        int[] expectedReputation = {1, 1, 1, 2, 3, 3};
        int[] expectedFunds = {15, 17, 20, 25, 28, 33};
        for (int idx = 0; idx < tilesToAdd.length; idx++) {
            assertEquals(player.getScore(), expectedScore[idx]);
            assertEquals(player.getIncome(), expectedIncome[idx]);
            assertEquals(player.getReputation(), expectedReputation[idx]);
            assertEquals(player.getFunds(), expectedFunds[idx]);
            game.PlaceTile(player, targetTiles.get(tilesToAdd[idx]), positionsToAdd[idx]);
            player.applyStats();
        }
        assertEquals(player.getScore(), expectedScore[tilesToAdd.length]);
        assertEquals(player.getIncome(), expectedIncome[tilesToAdd.length]);
        assertEquals(player.getReputation(), expectedReputation[tilesToAdd.length]);
        assertEquals(player.getFunds(), expectedFunds[tilesToAdd.length]);
    }

    private Game game;
}