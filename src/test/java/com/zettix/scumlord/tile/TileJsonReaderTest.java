package com.zettix.scumlord.tile;

import com.zettix.scumlord.PlayerStatChange;
import com.zettix.scumlord.tile.enums.SlumColors;
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
        assertEquals(tiles.size(), 47);

        Tile tile = tiles.get(0);
        PlayerStatChange res = tile.getActions().get(0).getChange();
        assertEquals(res.getFundsChange(), 0);
        assertEquals(tile.getColor(), SlumColors.GREEN);
        assertEquals(res.getPopulationChange(), 2);

        String[] expecteds = {
                "Suburbs [$: 3 ][c: Green]<<[p:2]>>",
                "Heavy Factory [$: 3 ][c: Yellow]<<[i:1]>>< when Green Gray  is adjacent <[r:-1]>>",
                "Community Park [$: 4 ][c: Gray]<<[i:-1]>>< when Green Yellow Blue  is adjacent <[r:1]>>",
                "Business Supply Store [$: 8 ][c: Blue]<<[i:1]>>< when Office  of all tiles <[i:1]>>",
                "Fast Food Restaurant [$: 7 ][c: Blue ][t: Restaurant]<<[i:1]>>< when Green  is adjacent <[p:1]>>",
                "Mint [$: 15 ][c: Gray]<<[i:3]>>< when Gray  of player tiles <[$:2]>>",
                "Parking Lot [$: 12 ][c: Blue]<<[i:1]>>< when Gray Blue  is adjacent <[i:1]>>",
                "Convenience Store [$: 6 ][c: Blue]<<[i:1]>>"
        };
        for (int i = 0; i < expecteds.length; i++) {
            Tile testTile = tiles.get(i);
            String expected = expecteds[i];
            assertEquals(expected, testTile.toString());
        }
    }
}
