package com.zettix.scumlord.tile;

import java.util.HashMap;
import java.util.Map;

public enum TileSeries {
    START,
    INTERNAL,
    A,
    B,
    C;

    public static TileSeries fromString(String inType) {
        String lowType = inType.toLowerCase();
        Map<String, TileSeries> typeMap = new HashMap<>();
        for (TileSeries effect : values()) {
            typeMap.put(effect.toString().toLowerCase(), effect);
        }
        if (typeMap.containsKey(lowType)) {
            return typeMap.get(lowType);
        }
        throw new IllegalArgumentException("Unknown Series Type:" + inType);
    }
}
