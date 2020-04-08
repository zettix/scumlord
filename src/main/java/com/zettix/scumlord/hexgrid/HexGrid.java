package com.zettix.scumlord.hexgrid;

// A HexGrid is a grid on a hexagon.
// This is isometric to square grid, see image.
// For the purposes of the game, limits are added.

// Hex: /-\
//    : \_/


// Hexes:   _   _   _   _
//         / \_/2\_/2\_/2\_/
//         \_/2|1/2\3/2\5/2\
//         / \0/1\2/1\4/1\6/
//         \_/1|1/1\3/1\5/1\
//         / \0/0\2/0\4/0\6/
//         \_/0|1/0\3/0\5/0\
//         / \0/ \2/ \4/ \6/
// These are X-hexes, one side aligned with X-axis.
// For a given x, y, consideration must be given to
// the even/odd nature of the coordinates.
// For an X hex with side length 1, the height is:  2 * sqrt (1 + 1/2 * 1/2) ~= 2.236
// so if x even, y = y * 2.236, x = x * 3
//    if y even, y = y * 2.236 + 1.118, x = 1.118 + x * 3
//


import com.zettix.scumlord.tile.Tile;

import java.util.*;

public class HexGrid {

    public HexGrid() {
        grid = new HashMap<>();
    }

    public List<HexPosition> getOpenPositions() {
        List<HexPosition> results = new ArrayList<>();
        Set<HexPosition> positions = new HashSet<>();
        Set<HexPosition> used = grid.keySet();
        for (HexPosition hexPosition : used) {
            Set<HexPosition> neighbors = hexPosition.getNeighbors();
            for (HexPosition h : neighbors) {
                if (used.contains(h)) {
                    continue;
                }
                // TODO(sean): Additional validation of hex placement. e.g. requirements.
                positions.add(h);
            }
        }
        for (HexPosition position : positions) {
            results.add(position);
        }
        return results;
    }

    public Set<HexPosition> getLocations() {
        return grid.keySet();
    }

    public Tile getTile(HexPosition position) {
        if (grid.containsKey(position)) {
            return grid.get(position);
        }
        return null;
    }

    public void setTile(Tile tile, HexPosition position) {
        grid.put(position, tile);
    }

    private final Map<HexPosition, Tile> grid;
}
