package com.zettix.scumlord;

import com.zettix.scumlord.hexgrid.HexGrid;
import com.zettix.scumlord.tile.TileSeries;
import org.junit.Before;
import org.junit.Test;

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

    private Game game;

}