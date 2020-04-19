package com.zettix.scumlord.tile;

import com.zettix.scumlord.tile.enums.SlumColors;
import com.zettix.scumlord.tile.enums.TileAction;
import com.zettix.scumlord.tile.enums.TileSeries;
import com.zettix.scumlord.tile.enums.TileTag;

import java.util.ArrayList;
import java.util.List;

public class TileImpl implements Tile {

    public TileImpl() {
        // DATA
        name = "Default";
        text = "-";
        series = TileSeries.START;
        cost = 0;
        color = SlumColors.GREEN;
        tileTag = TileTag.NONE;
        count = 0;
        // Requirements and effects
        actions = new ArrayList<>();
        requirement = null;
    }

    // Setters
    public Tile setName(String name) {
        this.name = name;
        return this;
    }

    public Tile setSeries(TileSeries series) {
        this.series = series;
        return this;
    }

    public Tile setText(String text) {
        this.text = text;
        return this;
    }

    public Tile setCost(int val) {
        this.cost = val;
        return this;
    }
    public Tile setCount(int val) {
        this.count = val;
        return this;
    }
    public Tile setColor(SlumColors color) {
        this.color = color;
        return this;
    }
    public Tile setTileTag(TileTag tag) {
        tileTag = tag;
        return this;
    }

    public Tile setTileActions(List<TileAction> actions) {
        this.actions = actions;
        return this;
    }

    public Tile setRequirement(TileAction requirement) {
        this.requirement = requirement;
        return this;
    }


    // Getters:
    public String getName() { return name; }
    public TileSeries getSeries() { return series; }
    public String getText() { return text; }
    public int getCost() { return cost; }
    public SlumColors getColor() { return color; }
    public TileTag getTileTag() {return tileTag;}
    public List<TileAction> getActions() { return actions; }
    public TileAction getRequirement() { return requirement; }
    public int getCount() { return count; }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (!text.equals("-")) {
            sb.append(" [T: ");
            sb.append(text);
            sb.append("]");
        }
        sb.append(" [$: ");
        sb.append(cost);
        sb.append(" ][c: ");
        sb.append(color);
        if (tileTag != TileTag.NONE) {
            sb.append(" ][t: ");
            sb.append(tileTag);
        }
        sb.append("]");
        for (TileAction action : actions) {
            sb.append("<");
            sb.append(action);
            sb.append(">");
        }
        return sb.toString();
    }

    // Private data:
    private String name;
    private TileSeries series;
    private String text;
    private int cost;
    private int count;
    private SlumColors color;
    private TileTag tileTag;
    List<TileAction> actions;
    TileAction requirement;
}
