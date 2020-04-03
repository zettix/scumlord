package com.zettix.scumlord;

import org.junit.Assert;
import org.junit.Test;

public class PlayerBoardTest {

    @Test
    public void PlayerBoardInitTest() {
        PlayerBoard playerBoard = new PlayerBoard();
        Assert.assertNotNull(playerBoard);
    }

}