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
        HexGrid board = new HexGrid();
        Player player = new Player("Test Player", game);
        game.InitTiles(board, player);
        assertEquals(player.getFunds(), 0);
        assertEquals(player.getIncome(), 0);
        assertEquals(player.getReputation(), 0);
        assertEquals(player.getScore(), 2);
    }

    @Test
    public void initAllPlayerTiles() {
    }

    private Game game;

}