package com.zettix.scumlord;

import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class TileJsonReaderTest {

    @Test
    public void testLoad() {
        TileJsonReader reader = new TileJsonReader();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("test_tiles.json");
        reader.setInputStream(is);
        List<Tile> tiles = reader.Load();
        assertEquals(tiles.size(), 3);

        Tile tile = tiles.get(0);
        PlayerStatChange res = tile.getActions().get(0).getChange();
        assertEquals(res.getFundsChange(), 0);
        assertEquals(tile.getColor(), SlumColors.GREEN);
        assertEquals(res.getPopulationChange(), 3);
        String expected = "Suburbs [$: 3 ][c: Green]<<[p:3]>>";
        assertEquals(tile.toString(), expected);

        Tile tile2 = tiles.get(1);
        String expected2 = "Heavy Factory [$: 3 ][c: Yellow]<<[i:1]>>< when Green Gray  is adjacent <[r:-1]>>";
        assertEquals(tile2.toString(), expected2);

        Tile tile3 = tiles.get(2);
        String expected3 = "Community Park [$: 4 ][c: Gray]<<[i:-1]>>< when Green Yellow Blue  is adjacent <[r:1]>>";
        assertEquals(tile3.toString(), expected3);
    }
}