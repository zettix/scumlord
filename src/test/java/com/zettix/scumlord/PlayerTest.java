package com.zettix.scumlord;

import com.zettix.scumlord.hexgrid.HexPosition;
import com.zettix.scumlord.tile.*;
import com.zettix.scumlord.tile.enums.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class PlayerTest {

    Player player;

    @Before
    public void setUp() {

        player = new Player("Test Player Tim", new PlayerStatChange()
                .setReputationChange(1).setFundsChange(15));
    }

    @Test
    public void getName() {
        assertEquals(player.getName(), "Test Player Tim");
    }

    @Test
    public void getIncome() {
        assertEquals(0, player.getIncome());
    }

    @Test
    public void getFunds() {
        assertEquals(15, player.getFunds());
    }

    @Test
    public void getReputation() {
        assertEquals(1, player.getReputation());
    }

    @Test
    public void getScore() {
        assertEquals(0, player.getScore());
    }

    @Test
    public void testAddToGlobals() {
        Tile t = new TileImpl().setName("homeowner's association");
        HexPosition position =  new HexPosition(1,1);
        player.getBoard().setTile(t, position);
        player.addTileToGlobals(position);
        assertEquals(1, player.getGlobalTiles().size());
        assertTrue(player.getGlobalTiles().contains(position));
    }

    @Test
    public void testAddToAdjacents() {
        PlayerStatChange statChange = new PlayerStatChange().setReputationChange(-1);
        TileAction action = new TileAction(TileEffectType.COLOR,
                TileEffectTime.ONGOING, TileAreaEffect.ADJACENT, statChange);

        SortedSet<SlumColors> badColors = new TreeSet<>();
        action.setFilterColors(badColors);
        badColors.add(SlumColors.GREEN);
        List<TileAction> actionList = new ArrayList<>();
        actionList.add(action);
        Tile t = new TileImpl().setName("factory").setColor(SlumColors.YELLOW).setTileActions(actionList);
        HexPosition position =  new HexPosition(1,1);
        //player.getBoard().setTile(t, position);
        //player.addTileToAdjacents(position);
        player.addTile(t, position);
        Tile suburb = new TileImpl().setName("suburbs").setColor(SlumColors.GREEN);
        PlayerStatChange resultChange = player.addTile(suburb, new HexPosition(0,1));
        assertEquals(-1, resultChange.getReputationChange());
    }


    @Test
    public void testApplyStats() {
        player.applyStats();
        assertEquals(1, player.getScore());
    }

    @Test
    public void testApplyChange() {
        PlayerStatChange change = new PlayerStatChange().setFundsChange(10)
                .setReputationChange(3)
                .setIncomeChange(-3)
                .setPopulationChange(31);
        player.applyChange(change);
        assertEquals(31, player.getScore());
        assertEquals(25, player.getFunds());
        assertEquals(-3, player.getIncome());
        assertEquals(4, player.getReputation());
    }

    @Test
    public void getTilesByTag() {
        Tile airport1 = new TileImpl().setName("Airport1").setTileTag(TileTag.AIRPORT);
        Tile airport2 = new TileImpl().setName("Airport2").setTileTag(TileTag.AIRPORT);
        Tile airport3 = new TileImpl().setName("Airport3").setTileTag(TileTag.AIRPORT);
        Tile school1 = new TileImpl().setName("school1").setTileTag(TileTag.SCHOOL);
        Tile school2 = new TileImpl().setName("school2").setTileTag(TileTag.SCHOOL);
        player.getBoard().setTile(airport1, new HexPosition(0, 0));
        player.getBoard().setTile(airport2, new HexPosition(0, 1));
        player.getBoard().setTile(airport3, new HexPosition(0, 2));
        player.getBoard().setTile(school1, new HexPosition(1, 1));
        player.getBoard().setTile(school2, new HexPosition(1, 2));
        List<Tile> schools = player.getTilesByTag(TileTag.SCHOOL);
        assertEquals(2, schools.size());
        List<Tile> airports = player.getTilesByTag(TileTag.AIRPORT);
        assertEquals(3, airports.size());
        List<Tile> dealerships = player.getTilesByTag(TileTag.DEALERSHIP);
        assertEquals(0, dealerships.size());
    }

    @Test
    public void getTilesByColor() {
        Tile suburb1 = new TileImpl().setName("Suburbs").setColor(SlumColors.GREEN);
        Tile suburb2 = new TileImpl().setName("Suburbs").setColor(SlumColors.GREEN);
        Tile suburb3 = new TileImpl().setName("Suburbs").setColor(SlumColors.GREEN);
        Tile heavyfactory2 = new TileImpl().setName("school1").setColor(SlumColors.YELLOW);
        Tile heavyfactory3 = new TileImpl().setName("school2").setColor(SlumColors.YELLOW);
        player.getBoard().setTile(suburb1, new HexPosition(0, 0));
        player.getBoard().setTile(suburb2, new HexPosition(0, 1));
        player.getBoard().setTile(suburb3, new HexPosition(0, 2));
        player.getBoard().setTile(heavyfactory2, new HexPosition(1, 1));
        player.getBoard().setTile(heavyfactory3, new HexPosition(1, 2));
        List<Tile> greens = player.getTilesByColor(SlumColors.GREEN);
        assertEquals(3, greens.size());
        List<Tile> yellows = player.getTilesByColor(SlumColors.YELLOW);
        assertEquals(2, yellows.size());
        List<Tile> grays = player.getTilesByColor(SlumColors.GRAY);
        assertEquals(0, grays.size());
    }
}