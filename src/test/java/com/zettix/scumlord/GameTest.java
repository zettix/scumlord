package com.zettix.scumlord;

import com.zettix.scumlord.hexgrid.HexGrid;
import com.zettix.scumlord.hexgrid.HexPosition;
import com.zettix.scumlord.images.RenderBoard;
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
       // RenderBoard renderBoard = new RenderBoard(player.getBoard(), game, 1020, 1024);
       // renderBoard.Render("Setup");
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
                "Gas Station",
                "Elementary School",
                "University"
        };

        Map<String, Tile> targetTiles = new HashMap<>();
        for (String tileName : tilesToAdd) {
            System.out.println("Finding:" + tileName);
            Tile tile = game.getTileByName(tileName);
            for (String name : tilesToAdd) {
                if (tile.getName().equals(name)) {
                    targetTiles.put(name, tile);
                }
            }
        }

        HexPosition[] positionsToAdd = {
                new HexPosition(0, 3),
                new HexPosition(0, 4),
                new HexPosition(0, 5),
                new HexPosition(0, 6),
                new HexPosition(0, 7),
        };
        int[] expectedScore = {2, 3, 4, 5, 8, 11};
        int[] expectedIncome = {0, 2, 3, 4, 4, 6};
        int[] expectedReputation = {1, 1, 1, 1, 2, 3};
        int[] expectedFunds = {15, 17, 20, 24, 28, 34};
        for (int idx = 0; idx < tilesToAdd.length; idx++) {
            System.out.println("idx:"+idx);
            assertEquals(player.getScore(), expectedScore[idx]);
            assertEquals(player.getIncome(), expectedIncome[idx]);
            assertEquals(player.getReputation(), expectedReputation[idx]);
            assertEquals(player.getFunds(), expectedFunds[idx]);
            game.PlaceTile(player, targetTiles.get(tilesToAdd[idx]), positionsToAdd[idx]);
            player.applyStats();
            //RenderBoard renderBoard = new RenderBoard(player.getBoard(), game, 240, 2000);
            //renderBoard.Render("testAdd5-idx:" + idx);
        }
        assertEquals(player.getScore(), expectedScore[tilesToAdd.length]);
        assertEquals(player.getIncome(), expectedIncome[tilesToAdd.length]);
        assertEquals(player.getReputation(), expectedReputation[tilesToAdd.length]);
        assertEquals(player.getFunds(), expectedFunds[tilesToAdd.length]);
    }

    @Test
    public void testAddAdjacents() {
        game.Load();
        PlayerStatChange change = new PlayerStatChange().setFundsChange(15);
        Player player = new Player("Test Player", change);
        game.AddPlayer(player);
        game.Setup();
        String[] tilesToAdd = {
                "Domestic Airport",
                "Boutique",
                "Gas Station",
                "Elementary School",
                "University"
        };
        Map<String, Tile> targetTiles = new HashMap<>();
        for (String tileName : tilesToAdd) {
            System.out.println("Finding:" + tileName);
            Tile tile = game.getTileByName(tileName);
            for (String name : tilesToAdd) {
                if (tile.getName().equals(name)) {
                    targetTiles.put(name, tile);
                }
            }
        }

        HexPosition[] positionsToAdd = {
                new HexPosition(1, 2),
                new HexPosition(-1, 1),
                new HexPosition(1, 1),
                new HexPosition(-1, 0),
                new HexPosition(1, 0),
        };
        int[] expectedReputation = { 1,  1,  2,  3,  4,  5};
        int[] expectedScore      = { 2,  3,  5,  8,  13,  18};
        int[] expectedIncome     = { 0,  2,  3,  4,  4,  6};
        int[] expectedFunds      = {15, 17, 20, 24, 28, 36};
        //////////////////////////// X.air,bou,gas,ele,
        for (int idx = 0; idx < tilesToAdd.length; idx++) {
            System.out.println("idx:"+idx);
            assertEquals(player.getIncome(), expectedIncome[idx]);
            assertEquals(player.getReputation(), expectedReputation[idx]);
            assertEquals(player.getFunds(), expectedFunds[idx]);
            assertEquals(player.getScore(), expectedScore[idx]);
            System.err.println("Placing Tile:" + targetTiles.get((tilesToAdd[idx])));
            game.PlaceTile(player, targetTiles.get(tilesToAdd[idx]), positionsToAdd[idx]);
            player.applyStats();
            //RenderBoard renderBoard = new RenderBoard(player.getBoard(), game, 800, 800);
            //renderBoard.Render("testAddAjectents" + idx);
        }
        assertEquals(player.getScore(), expectedScore[tilesToAdd.length]);
        assertEquals(player.getIncome(), expectedIncome[tilesToAdd.length]);
        assertEquals(player.getReputation(), expectedReputation[tilesToAdd.length]);
        //RenderBoard renderBoard = new RenderBoard(player.getBoard(), game, 800, 800);
        //renderBoard.Render("testAddAjectents");
    }

    private Game game;
}