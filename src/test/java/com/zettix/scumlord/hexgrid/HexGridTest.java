package com.zettix.scumlord.hexgrid;

import com.zettix.scumlord.Game;
import com.zettix.scumlord.Player;
import com.zettix.scumlord.PlayerStatChange;
import com.zettix.scumlord.hexgrid.HexGrid;
import com.zettix.scumlord.hexgrid.HexPosition;
import com.zettix.scumlord.images.RenderBoard;
import com.zettix.scumlord.tile.SlumColors;
import com.zettix.scumlord.tile.Tile;
import com.zettix.scumlord.tile.TileImpl;
import com.zettix.scumlord.tile.TileSeries;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class HexGridTest {

    Game game;
    Tile suburb;
    Player player;
    HexGrid grid;


    @Before
    public void setUp() {
        // This suburb is missing the "add 3 people" action.
        game = new Game();
        game.Load();
        List<Tile> starts = game.getTilesBySeries(TileSeries.START);
        for (Tile tile : starts) {
            if (tile.getName().equalsIgnoreCase("Suburbs")) {
                suburb = tile;
                break;
            }
        }
        player = new Player("test player", new PlayerStatChange());
        grid = player.getBoard();
    }

    @Test
    public void getSimpleEvenPositions() {
        Player player = new Player("test player", new PlayerStatChange());
        HexGrid grid = player.getBoard();
        HexPosition hexPosition = new HexPosition(0, 1);
        grid.setTile(suburb, hexPosition);
        List<HexPosition> openPositions = grid.getOpenPositions();
        assertEquals(openPositions.size(), 6);  // a naked tile.
        RenderBoard renderBoard = new RenderBoard(player, game, 1000, 1000);
        // renderBoard.Render("getSimpleEvenPositions");
    }

    public void getSimpleOddPositions() {
        HexGrid grid = new HexGrid();
        HexPosition hexPosition = new HexPosition(1, 1);
        grid.setTile(suburb, hexPosition);
        List<HexPosition> openPositions = grid.getOpenPositions();
        assertEquals(openPositions.size(), 6);  // a naked tile.
        RenderBoard renderBoard = new RenderBoard(player, game, 1000, 1000);
        // renderBoard.Render("getSimpleOddPositions");
    }

    @Test
    public void getOpenPositions() {
        HexGrid grid = new HexGrid();
        HexPosition hexPosition = new HexPosition(0, 1);
        grid.setTile(suburb, hexPosition);
        List<HexPosition> openPositions = grid.getOpenPositions();
        assertEquals(openPositions.size(), 6);  // a naked tile.
        HexPosition hexPos2 = new HexPosition(-1, 1);
        grid.setTile(suburb, hexPos2);
        HexPosition hexPos3 = new HexPosition(0, 2);
        grid.setTile(suburb, hexPos3);
        assertEquals(grid.getLocations().size(), 3);
        // with 3 suburbs in a tight triangle, the number of spots should be... 9
        openPositions = grid.getOpenPositions();
        assertEquals(openPositions.size(), 9);

        RenderBoard renderBoard = new RenderBoard(player, game, 1000, 1000);
        // renderBoard.Render("getOpenPositions");
    }

    @Test
    public void setTile() {
        HexGrid grid = new HexGrid();
        HexPosition hexPosition = new HexPosition(0, 0);
        grid.setTile(suburb, hexPosition);
        Tile t = grid.getTile(hexPosition);
        assertEquals(t.getName(), suburb.getName());
        Tile t2 = grid.getTile(new HexPosition(1, 1));
        assertNull(t2);
    }
}