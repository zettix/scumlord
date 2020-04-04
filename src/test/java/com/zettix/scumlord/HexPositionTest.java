package com.zettix.scumlord;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class HexPositionTest {


    HexPosition hexPosition;

    @Before
    public void setUp() throws Exception {
        hexPosition = new HexPosition(1, 2);
    }

    @Test
    public void getX() {
        assertEquals(hexPosition.getX(), 1);
    }

    @Test
    public void getY() {
        assertEquals(hexPosition.getY(), 2);
    }

    @Test
    public void getNeighbors() {
        List<HexPosition> positions = hexPosition.getNeighbors();
        List<HexPosition> expected = new ArrayList<>();
        expected.add(new HexPosition(0, 2));
        expected.add(new HexPosition(1, 3));
        expected.add(new HexPosition(2, 2));
        expected.add(new HexPosition(2, 1));
        expected.add(new HexPosition(1, 1));
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(positions.get(i), expected.get(i));
        }
    }

    @Test
    public void toString1() {
        assertEquals(hexPosition.toString(), "{x:1,y:2}");
    }

    @Test
    public void hashCode1() {
        assertEquals(hexPosition.hashCode(), 1015);
    }

    @Test
    public void equals1() {
        HexPosition hexPosition2 = new HexPosition(1, 2);
        assertEquals(hexPosition, hexPosition2);

        HexPosition hexPosition3 = new HexPosition(1, 1);
        assertNotEquals(hexPosition, hexPosition3);
    }

    @Test
    public void copy1() {
        HexPosition hexPosition2 = new HexPosition(hexPosition);
        assertEquals(hexPosition, hexPosition2);
    }
}