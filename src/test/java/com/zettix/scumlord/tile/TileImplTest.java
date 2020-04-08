package com.zettix.scumlord.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import com.zettix.scumlord.PlayerStatChange;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TileImplTest {

    private static final Logger LOG = Logger.getLogger(TileImplTest.class.getName());


    @Test
    public void suburbTileTest() {
        //tile.setText("A green suburb.");
        PlayerStatChange threePeople = new PlayerStatChange();
        threePeople.setPopulationChange(3);

        List<TileAction> actions = new ArrayList<>();
        TileAction action = new TileAction(TileEffectType.ANY, TileEffectTime.INSTANT, TileAreaEffect.ANY, threePeople);
        actions.add(action);

        Tile tile = new TileImpl().setName("Suburbs").setCost(3).setColor(SlumColors.GREEN).setTileActions(actions);


        PlayerStatChange res = tile.getActions().get(0).getChange();
        assertEquals(res.getFundsChange(), 0);
        assertEquals(tile.getColor(), SlumColors.GREEN);
        assertEquals(res.getPopulationChange(), 3);
        String expected = "Suburbs [$: 3 ][c: Green]<<[p:3]>>";
        assertEquals(tile.toString(), expected);
    }

    @Test
    public void factoryTest() {
        List<TileAction> actions = new ArrayList<>();

        PlayerStatChange change = new PlayerStatChange();
        change.setIncomeChange(1);
        TileAction action = new TileAction(TileEffectType.ANY, TileEffectTime.INSTANT, TileAreaEffect.ANY,
                change);
        actions.add(action);

        PlayerStatChange badEffect = new PlayerStatChange();
        badEffect.setReputationChange(-1);
        SortedSet<SlumColors> badColors = new TreeSet<>();
        badColors.add(SlumColors.GREEN);
        badColors.add(SlumColors.GRAY);
        TileAction action2 = new TileAction(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.ADJACENT,
          badEffect);
        action2.setFilterColors(badColors);
        actions.add(action2);

        Tile tile = new TileImpl();
        tile.setName("Heavy Factory").setCost(3).setColor(SlumColors.YELLOW).setTileActions(actions);

        String expected = "Heavy Factory [$: 3 ][c: Yellow]<<[i:1]>>< when Green Gray  is adjacent <[r:-1]>>";
        assertEquals(tile.toString(), expected);
    }

    @Test
    public void parkTest() {
        Tile tile = new TileImpl();
        tile.setName("Community Park");
        tile.setText("-");
        tile.setCost(4);
        tile.setColor(SlumColors.GRAY);
        PlayerStatChange change = new PlayerStatChange();
        change.setIncomeChange(-1);

        List<TileAction> actions = new ArrayList<>();

        TileAction action = new TileAction(TileEffectType.ANY, TileEffectTime.INSTANT, TileAreaEffect.ANY,
                change);
        actions.add(action);


        PlayerStatChange boost = new PlayerStatChange();
        boost.setReputationChange(1);

        TileAction action2 = new TileAction(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.ADJACENT,
                boost);
        SortedSet<SlumColors> goodColors = new TreeSet<>();
        goodColors.add(SlumColors.BLUE);
        goodColors.add(SlumColors.GREEN);
        goodColors.add(SlumColors.YELLOW);
        action2.setFilterColors(goodColors);

        actions.add(action2);
        tile.setTileActions(actions);

        assertEquals(tile.getName(), "Community Park");
        assertEquals(tile.getText(), "-");
        assertEquals(tile.getCost(), 4);
        assertEquals(tile.getColor(), SlumColors.GRAY);
        assertEquals(tile.getTileTag(), TileTag.NONE);


        List<TileAction> resultActions = tile.getActions();
        assertEquals(resultActions.get(0).getChange().getIncomeChange(), -1);
        assertEquals(
                resultActions.get(1).getChange().getReputationChange(), 1);
        String ex = "Community Park [$: 4 ][c: Gray]<<[i:-1]>>< when Green Yellow Blue  is adjacent <[r:1]>>";
        String res = tile.toString();
        assertEquals(res, ex);
    }
}
