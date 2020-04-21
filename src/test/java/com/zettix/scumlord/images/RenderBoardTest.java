package com.zettix.scumlord.images;

import com.zettix.scumlord.Game;
import com.zettix.scumlord.Player;
import com.zettix.scumlord.PlayerStatChange;
import com.zettix.scumlord.hexgrid.HexGrid;
import com.zettix.scumlord.hexgrid.HexPosition;
import com.zettix.scumlord.tile.Tile;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class RenderBoardTest {

    @Test
    public void render() {
        Game g = new Game();
        Player player = new Player("testPlayer", new PlayerStatChange());
        HexGrid board = player.getBoard();
        g.Load();
        Set<String> tilenames = g.getTileNames();
        int sz = tilenames.size();
        int i = 1;
        while (i * i < sz) {
            i++;
        }
        List<String> tileList = new ArrayList<>();
        tileList.addAll(tilenames);
        Collections.sort(tileList);
        int x = 0;
        int y = 0;
        int idx = 0;
        int xoff = -3;
        int yoff = 1;
        while (idx < tileList.size()) {
            Tile tile = g.getTileByName(tileList.get(idx));
            HexPosition position = new HexPosition(x + xoff, y + yoff);
            board.setTile(tile, position);
            x++;
            if (x > i) {
                x = 0;
                y++;
            }
            idx++;
        }
        RenderBoard renderBoard = new RenderBoard(player, g);
        //renderBoard.Render("AllTiles", 1800, 1800);
    }
}