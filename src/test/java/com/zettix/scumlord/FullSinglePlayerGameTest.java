package com.zettix.scumlord;
import static org.junit.Assert.*;

import com.zettix.scumlord.hexgrid.HexPosition;
import com.zettix.scumlord.images.RenderBoard;
import com.zettix.scumlord.tile.Tile;
import org.junit.Test;

/**
 *  FullSinglePlayerGameTest is a mockup of the eventual full game.
 *
 *  The purpose is to get the rest of the modules to work together.
 *
 *  Before a game starts, the following must be known:
 *  1) The number of players.
 *  2) The game, variant, rules, or settings.
 *  Then the game starts:
 *  1) Game setup takes place:
 *  1.A) Any cards/tiles/initialization takes place.
 *  1.B) All player initial resources are allocated.
 *  1.C) The scoreboard is initialized.
 *  1.D) The market is initialized.
 *
 *  Then the game loop takes place:
 *  For each player, perform the following steps:
 *
 *  1.A) Purchase or lake a tile.
 *  1.A.i) Price includes tax.
 *  1.B) Cleanup purchase:
 *  1.B.i) if normal tile, remove from market.
 *  1.B.ii) if laking normal tile, remove from market.
 *  1.B.ii) if starter tile, select normal tile to destroy.
 *
 *  2.A) Place the tile.
 *  2.A.i) Select valid placement position.
 *  2.A.ii) Add tile to player board.
 *  2.B) Perform immediate actions related to the tile.
 *  2.B.i) Update player statistics based on immediate actions.
 *  2.B.ii) Reputation, income, score, or funds.
 *  2.B.iii) Adjust player statistics based on number of red lines crossed.
 *  2.C) Perform other actions related to tile, adjacent, global, etc.
 *  2.D) Update player statistics and collect income in the following order:
 *  2.D.i) Update player funds, income, and reputation.
 *  2.D.ii) Player adds to funds amount indicated by income.
 *  2.D.iii) Update player score.
 *  2.D.iv) Adjust player statistics based on number of red lines crossed.
 *
 *  3.0) End turn and go to next player until round is over.
 *  3.0.i) Player ends turn.
 *  3.0.ii) Game end test condition.
 *  3.0.iii) Next player or next round starting with player 1.
 *
 */

public class FullSinglePlayerGameTest {

    public void MegaAssert(int score, int income, int reputation, int funds, Player player, int iter) {
        String msg = "iteration:" + iter;
        assertEquals(msg, score, player.getScore());
        assertEquals(msg, income, player.getIncome());
        assertEquals(msg, reputation, player.getReputation());
        assertEquals(msg, funds, player.getFunds());
    }

    @Test
    public void singlePlayerTest() {
        Game game = new Game();
        game.doShuffule = false;
        PlayerStatChange change = new PlayerStatChange().setFundsChange(15);
        Player player = new Player("Test player Mc. Winface", change);
        game.Load();
        game.AddPlayer(player);
        game.Setup();
        MegaAssert(2, 0,1, 15, player, 1);
        Tile t = game.buyTile(player, "Suburbs:1");
        MegaAssert(2, 0,1, 12, player, 1);
        game.PlaceTile(player, t, new HexPosition(1, 0));
        MegaAssert(6, 0,2, 12, player, 1);

        t = game.buyTile(player, "Tile:0"); // Business Supply Store, $8, +1inc.
        System.err.println("Purchasing tile:" + t.toString());
        MegaAssert(6, 0,2, 4, player, 2);
        game.PlaceTile(player, t, new HexPosition(2, 0));
        MegaAssert(8, 1,2, 5, player, 2);

        t = game.buyTile(player, "Lake:0"); // Lake
        System.err.println("Purchasing tile:" + t.toString());
        MegaAssert(8, 1,2, 5, player, 3);
        game.PlaceTile(player, t, new HexPosition(-1, 0));
        MegaAssert(10, 1,2, 10, player, 3);

        t = game.buyTile(player, "Tile:0"); // Fast food, +1 inc, +1 score/gren
        System.err.println("Purchasing tile:" + t.toString());
        MegaAssert(10, 1,2, 3, player, 4);
        game.PlaceTile(player, t, new HexPosition(1, 1));
        MegaAssert(14, 2,3, 5, player, 4);

        t = game.buyTile(player, "Lake:0"); // Lake
        System.err.println("Purchasing tile:" + t.toString());
        MegaAssert(14, 2,3, 5, player, 5);
        game.PlaceTile(player, t, new HexPosition(2, 1));
        MegaAssert(17, 2,3, 13, player, 5);

        t = game.buyTile(player, "Tile:1"); // Parking Lot
        System.err.println("Purchasing tile:" + t.toString());
        MegaAssert(17, 2,3, 1, player, 6);
        game.PlaceTile(player, t, new HexPosition(-1, 1));
        MegaAssert(21, 4,4, 7, player, 6);

        t = game.buyTile(player, "Heavy Factory:1");
        MegaAssert(21, 4,4, 4, player, 7);
        game.PlaceTile(player, t, new HexPosition(-2, 0));
        MegaAssert(25, 5,4, 11, player, 7);

        t = game.buyTile(player, "Tile:1");
        System.err.println("Purchasing tile:" + t.toString());
        MegaAssert(25, 5,4, 5, player, 8);
        game.PlaceTile(player, t, new HexPosition(-2, 1));
        MegaAssert(29, 7,4, 14, player, 8);

        t = game.buyTile(player, "Tile:1");
        System.err.println("Purchasing tile:" + t.toString());
        MegaAssert(29, 7,4, 8, player, 9);
        game.PlaceTile(player, t, new HexPosition(-1, 2));
        MegaAssert(33, 9,4, 17, player, 9);

        t = game.buyTile(player, "Tile:1");
        System.err.println("Purchasing tile:" + t.toString());
        MegaAssert(33, 9,4, 12, player, 10);
        game.PlaceTile(player, t, new HexPosition(-2, 2));
        MegaAssert(37, 12,4, 24, player, 10);

        t = game.buyTile(player, "Suburbs:1");
        MegaAssert(37, 12,4, 21, player, 11);
        game.PlaceTile(player, t, new HexPosition(2, 2));
        MegaAssert(44, 12,4, 35, player, 11);

        t = game.buyTile(player, "Tile:3");
        System.err.println("Purchasing tile:" + t.toString());
        MegaAssert(44, 12,4, 21, player, 12);
        game.PlaceTile(player, t, new HexPosition(1, 2));
        MegaAssert(52, 12,4, 33, player, 12);

        t = game.buyTile(player, "Tile:0");
        System.err.println("Purchasing tile:" + t.toString());
        MegaAssert(52, 12,4, 18, player, 13);
        game.PlaceTile(player, t, new HexPosition(-1, 3));
        MegaAssert(56, 15,4, 37, player, 13);

        t = game.buyTile(player, "Suburbs:1");
        MegaAssert(56, 15,4, 34, player, 14);
        game.PlaceTile(player, t, new HexPosition(2, 3));
        MegaAssert(63, 15,5, 49, player, 14);

        t = game.buyTile(player, "Tile:0");
        System.err.println("Purchasing tile:" + t.toString());
        MegaAssert(63, 15,5, 45, player, 15);
        game.PlaceTile(player, t, new HexPosition(1, 3));
        MegaAssert(75, 15,6, 60, player, 15);

        t = game.buyTile(player, "Tile:6");
        System.err.println("Purchasing tile:" + t.toString());
        MegaAssert(75, 15,6, 40, player, 16);
        game.PlaceTile(player, t, new HexPosition(-2, 3));
        MegaAssert(81, 20,6, 62, player, 16);

        t = game.buyTile(player, "Tile:6");
        System.err.println("Purchasing tile:" + t.toString());
        MegaAssert(81, 20,6, 43, player, 17);
        game.PlaceTile(player, t, new HexPosition(0, 3));
        MegaAssert(86, 23,5, 68, player, 17);


        RenderBoard renderBoard = new RenderBoard(player, game);
        //renderBoard.Render("singlePlayerTest", 1040, 980);
    }
}
