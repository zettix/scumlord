package com.zettix.scumlord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileImpl implements Tile {

    public TileImpl() {
        name = "Default";
        text = "-";
        cost = 0;
        color = SlumColors.GREEN;
        tileTag = TileTag.NONE;
        actions = new ArrayList<>();
        requirement = null;
        tier = "A";
    }

    // Setters
    public Tile setName(String name) {
        this.name = name;
        return this;
    }

    public Tile setTier(String tier) {
        this.tier = tier;
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
    public String getTier() { return tier; }
    public String getText() { return text; }
    public int getCost() { return cost; }
    public SlumColors getColor() { return color; }
    public TileTag getTileTag() {return tileTag;}
    public List<TileAction> getActions() { return actions; }
    public TileAction getRequirement() { return requirement; }


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
    private String tier;
    private String text;
    private int cost;
    private SlumColors color;
    private TileTag tileTag;
    List<TileAction> actions;
    TileAction requirement;
}
