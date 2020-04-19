package com.zettix.scumlord.tile.enums;

import java.util.HashMap;
import java.util.Map;

public enum SlumColors {
    GREEN,
    YELLOW,
    GRAY,
    BLUE,
    OCEAN;

    @Override
    public String toString() {
       switch(this) {
           case GREEN:
               return "Green";
           case YELLOW:
               return "Yellow";
           case GRAY:
               return "Gray";
           case BLUE:
               return "Blue";
           case OCEAN:
               return "Ocean";
           default:
               return "Unknown Color";
       }
    }

    public static SlumColors fromString(String color) {
        String lowColor = color.toLowerCase();
        Map<String, SlumColors> slumColorsMap = new HashMap<>();
        for (SlumColors slumColor : values()) {
            slumColorsMap.put(slumColor.toString().toLowerCase(), slumColor);
        }
        if (slumColorsMap.containsKey(lowColor)) {
            return slumColorsMap.get(color);
        }
        throw new IllegalArgumentException("Unknown color:" + color);
    }

}
