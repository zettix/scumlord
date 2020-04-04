package com.zettix.scumlord;

import java.io.Serializable;
import java.util.List;

public interface Tile extends Serializable {

    // Setters
    Tile setName(String name);          // The Name of the tile.
    Tile setTier(String tier);          // The Tier of the tile (A, B, C)
    Tile setText(String name);          // Any coloring text.
    Tile setCost(int val);              // The cost of the tile.
    Tile setColor(SlumColors color);    // The color: Green, Blue, Gray, Ocean, etc.
    Tile setTileTag(TileTag tag);       // The tag: Office, Restaurant, Airport, etc.
    Tile setTileActions(List<TileAction> actions);  // Anything the tile does.
    Tile setRequirement(TileAction requriement);  // Requirements to place tile.

    // Getters
    String getName();
    String getTier();
    String getText();
    int getCost();
    SlumColors getColor();
    TileTag getTileTag();
    List<TileAction> getActions();
    TileAction getRequirement();
}
