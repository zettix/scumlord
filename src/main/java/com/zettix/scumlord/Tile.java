package com.zettix.scumlord;

import java.util.List;

public interface Tile {

    // Setters
    Tile setName(String name);          // The Name of the tile.
    Tile setText(String name);          // Any coloring text.
    Tile setCost(int val);              // The cost of the tile.
    Tile setColor(SlumColors color);    // The color: Green, Blue, Gray, Ocean, etc.
    Tile setTileTag(TileTag tag);       // The tag: Office, Restaurant, Airport, etc.
    Tile setTileActions(List<TileAction> actions);  // Anything the tile does.

    // Getters
    String getName();
    String getText();
    int getCost();
    SlumColors getColor();
    TileTag getTileTag();
    List<TileAction> getActions();
}
