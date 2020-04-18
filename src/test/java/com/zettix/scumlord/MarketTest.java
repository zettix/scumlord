package com.zettix.scumlord;

import com.zettix.scumlord.tile.Tile;
import com.zettix.scumlord.tile.TileSeries;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MarketTest {

    private Game game;
    private Market market;


    @Before
    public void Setup() {
        game = new Game();
        game.Load();
        market = new Market(2, game, false);
        market.Setup();
    }

    @Test
    public void testBuyStarter() {
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
        Integer[] expected = {7, 12, 14};
        TileSeries[] testSeries = {TileSeries.A, TileSeries.B, TileSeries.C};
        Map<TileSeries, Integer> seriesCounts = market.getSeriesTileCounts();
        for (int i  = 0; i < expected.length; i++) {
            assertEquals(expected[i], seriesCounts.get(testSeries[i]));
        }
        assertEquals(7, market.getCounterTop().size());
    }


    @Test
    public void testBuyTiles() {
        List<Tile> oldCounter = market.getCounterTop();
        for (int i = 0; i < 7; i++) {
            Tile t = market.BuyTile(3);
            assertEquals(7, market.getCounterTop().size());
            int seriesAlen = market.getSeriesTileCounts().get(TileSeries.A);
            assertEquals(6 - i, seriesAlen);
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
            assertEquals(7, market.getCounterTop().size());
            int seriesBlen = market.getSeriesTileCounts().get(TileSeries.B);
            assertEquals(11 - i, seriesBlen);
        }
        // Buy up 12 more tiles.
        for (int i = 0; i < 12; i++) {
            Tile t = market.BuyTile(3);
            assertEquals(7, market.getCounterTop().size());
        }
        stats = market.getSeriesTileCounts();
        seriesLen = stats.get(TileSeries.A);
        assertEquals(0, seriesLen);
        seriesLen = stats.get(TileSeries.B);
        assertEquals(0, seriesLen);
        seriesLen = stats.get(TileSeries.C);
        assertEquals(8, seriesLen);
    }

    @Test
    public void NonShuffleTileTest() {
        String[] expected_layout =  {
                "Business Supply Store",
                "Business Supply Store",
                "Fast Food Restaurant",
                "Fast Food Restaurant",
                "Mint",
                "Mint",
                "Parking Lot",
        };
        List<Tile> counterTop = market.getCounterTop();
        for (int i = 0; i < expected_layout.length; i++) {
            assertEquals(expected_layout[i], counterTop.get(i).getName());
        }
    }

}