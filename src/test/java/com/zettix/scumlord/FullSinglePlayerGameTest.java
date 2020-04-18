package com.zettix.scumlord;
import static org.junit.Assert.*;
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

    @Test
    public void singlePlayerTest() {
        Game game = new Game();
        game.doShuffule = false;
        PlayerStatChange change = new PlayerStatChange().setFundsChange(15);
        Player player = new Player("Test player Mc. Winface", change);
        game.Load();
        game.AddPlayer(player);
        game.Setup();
        assertEquals(2, player.getScore());



    }

}
