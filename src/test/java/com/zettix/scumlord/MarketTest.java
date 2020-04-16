package com.zettix.scumlord;

import com.zettix.scumlord.tile.Tile;
import com.zettix.scumlord.tile.TileSeries;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MarketTest {

    @Test
    public void testBuyStarter() {
        Game game = new Game();
        game.Load();
        Market market = new Market(2, game);
        market.Setup();
        Tile suburbs = game.getTileByName("Suburbs");
        assertNotNull("Find Suburbs tile:", suburbs);
        int availableSuburbs = market.getStartTileCounts().get(suburbs);
        assertEquals("Available Suburbs:" ,4, availableSuburbs);
        Tile t = market.BuyStarterTile(suburbs);
        assertEquals(suburbs, t);
        availableSuburbs = market.getStartTileCounts().get(suburbs);
        assertEquals("Post buy Available Suburbs:" ,3, availableSuburbs);
    }


    @Test
    public void testStartTileCounts() {
        Game game = new Game();
        game.Load();
        Market market = new Market(2, game);
        market.Setup();
        Tile tile = game.getTileByName("Suburbs");
        Map<TileSeries, Integer> stackCounts = market.getSeriesTileCounts();
        int available = market.getStartTileCounts().get(tile);
        assertEquals("Available Suburbs:" ,4, available);

        tile = game.getTileByName("Heavy Factory");
        available = market.getStartTileCounts().get(tile);
        assertEquals("Available Heavy Factories:" ,4, available);

        tile = game.getTileByName("Community Park");
        available = market.getStartTileCounts().get(tile);
        assertEquals("Available Community Parks:" ,4, available);
    }

    @Test
    public void testCounterTileCounts() {
        Game game = new Game();
        game.Load();
        Market market = new Market(2, game);
        market.Setup();
        Integer[] expected = {6, 12, 14};
        TileSeries[] testSeries = {TileSeries.A, TileSeries.B, TileSeries.C};
        Map<TileSeries, Integer> seriesCounts = market.getSeriesTileCounts();
        for (int i  = 0; i < expected.length; i++) {
            assertEquals(expected[i], seriesCounts.get(testSeries[i]));
        }
        assertEquals(8, market.getCounterTop().size());
    }


    @Test
    public void testBuyTiles() {
        Game game = new Game();
        game.Load();
        Market market = new Market(2, game);
        market.Setup();
        List<Tile> oldCounter = market.getCounterTop();
        for (int i = 0; i < 6; i++) {
            Tile t = market.BuyTile(3);
            assertEquals(8, market.getCounterTop().size());
            int seriesAlen = market.getSeriesTileCounts().get(TileSeries.A);
            assertEquals(5 - i, seriesAlen);
        }
        // All A tiles are out.
        Map<TileSeries, Integer> stats = market.getSeriesTileCounts();
        int seriesLen = stats.get(TileSeries.A);
        assertEquals(0, seriesLen);
        seriesLen = stats.get(TileSeries.B);
        assertEquals(12, seriesLen);
        seriesLen = stats.get(TileSeries.C);
        assertEquals(14, seriesLen);
        // Buy up 6 B tiles.
        for (int i = 0; i < 6; i++) {
            Tile t = market.BuyTile(3);
            assertEquals(8, market.getCounterTop().size());
            int seriesBlen = market.getSeriesTileCounts().get(TileSeries.B);
            assertEquals(11 - i, seriesBlen);
        }
        // Buy up 12 more tiles.
        for (int i = 0; i < 12; i++) {
            Tile t = market.BuyTile(3);
            assertEquals(8, market.getCounterTop().size());
        }
        stats = market.getSeriesTileCounts();
        seriesLen = stats.get(TileSeries.A);
        assertEquals(0, seriesLen);
        seriesLen = stats.get(TileSeries.B);
        assertEquals(0, seriesLen);
        seriesLen = stats.get(TileSeries.C);
        assertEquals(8, seriesLen);
    }

}