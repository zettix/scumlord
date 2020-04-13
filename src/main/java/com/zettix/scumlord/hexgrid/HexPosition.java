package com.zettix.scumlord.hexgrid;

import java.util.HashSet;
import java.util.Set;

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

    private static final int[] EVEN_NEIGHBOR_MAP_X = {-1, 0, 1, 1, 0, -1};
    private static final int[] EVEN_NEIGHBOR_MAP_Y = { 0, 1, 0,-1,-1, -1};
    private static final int[] ODD_NEIGHBOR_MAP_X = {-1, 0, 1, 1, 0, -1};
    private static final int[] ODD_NEIGHBOR_MAP_Y = { 1, 1, 1, 0,-1, 0};

    public Set<HexPosition> getNeighbors() {
        Set<HexPosition> result = new HashSet<>();
        int[] neighborMapX = EVEN_NEIGHBOR_MAP_X;
        int[] neighborMapY = EVEN_NEIGHBOR_MAP_Y;
        int xt = (x < 0) ? -x : x;
        if (xt % 2 == 1) {
            neighborMapX = ODD_NEIGHBOR_MAP_X;
            neighborMapY = ODD_NEIGHBOR_MAP_Y;
        }
        for (int i = 0; i < 6; i++) {
            int dx = neighborMapX[i] + x;
            int dy = neighborMapY[i] + y;
            HexPosition newGuy = new HexPosition(dx, dy);
            result.add(newGuy);
        }
        return result;
    }

    // Based on hexagons of dimension 192x221.
    public int[] toGrid() {
        int xx = 0;
        int yy = 0;
        int xt = (x < 0) ? -x : x;
        if (xt % 2 == 1) {
            yy = 110;
        }
        xx += x * 192;
        yy += y * 221;
        int[] result = new int[2];
        result[0] = xx;
        result[1] = yy;
        return result;
    };

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
