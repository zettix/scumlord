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
        assertEquals(tiles.size(), 8);

        Tile tile = tiles.get(0);
        PlayerStatChange res = tile.getActions().get(0).getChange();
        assertEquals(res.getFundsChange(), 0);
        assertEquals(tile.getColor(), SlumColors.GREEN);
        assertEquals(res.getPopulationChange(), 3);

        String[] expecteds = {
                "Suburbs [$: 3 ][c: Green]<<[p:3]>>",
                "Heavy Factory [$: 3 ][c: Yellow]<<[i:1]>>< when Green Gray  is adjacent <[r:-1]>>",
                "Community Park [$: 4 ][c: Gray]<<[i:-1]>>< when Green Yellow Blue  is adjacent <[r:1]>>",
                "Domestic Airport [$: 11 ][c: Yellow ][t: Airport]<<[i:1]>>< when Green  is adjacent <[r:-1]>>< when Airport  of all tiles <[i:1]>>",
                "Boutique [$: 9 ][c: Blue]<<[i:1]>>< when Green  is adjacent <[r:1]>>",
                "Shipping Center [$: 15 ][c: Blue]<<[r:1]>>< when Blue  of all tiles <[$:2]>>",
                "Elementary School [$: 5 ][c: Gray ][t: School]<<[r:1]>>< when Green  of player tiles <[p:1]>>",
                "University [$: 15 ][c: Gray]<<[i:2]>>< when School  of player tiles <[r:1]>>"
        };
        for (int i = 0; i < expecteds.length; i++) {
            Tile testTile = tiles.get(i);
            String expected = expecteds[i];
            assertEquals(testTile.toString(), expected);
        }
    }
}