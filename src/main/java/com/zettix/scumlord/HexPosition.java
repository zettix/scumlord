package com.zettix.scumlord;

import java.util.ArrayList;
import java.util.List;

public class HexPosition {
    public HexPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public HexPosition(HexPosition h) {
        this.x = h.x;
        this.y = h.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private final int x;
    private final int y;

    private static final int[] neighborMapX = {-1, 0, 1, 1, 0, -1};
    private static final int[] neighborMapY = { 0, 1, 0,-1,-1, -1};

    public List<HexPosition> getNeighbors() {
        List<HexPosition> result = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int dx = neighborMapX[i] + x;
            int dy = neighborMapY[i] + y;
            HexPosition newGuy = new HexPosition(dx, dy);
            result.add(newGuy);
        }
        return result;
    }

    @Override
    public String toString() {
        return "{x:" + x + ",y:" + y + "}";
    }

    @Override
    public int hashCode() {
        return 1013 * x + y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof HexPosition) {
            HexPosition h = (HexPosition) o;
            if (h.x == x && h.y == y) {
                return true;
            }
        }
        return false;
    }
}
