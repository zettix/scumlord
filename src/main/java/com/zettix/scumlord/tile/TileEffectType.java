package com.zettix.scumlord.tile;

import java.util.HashMap;
import java.util.Map;

public enum TileEffectType {
    ANY,
    COLOR,
    NAME,
    TAG,
    RED_LINE;

    public static TileEffectType fromString(String inType) {
        String lowType = inType.toLowerCase();
        Map<String, TileEffectType> typeMap = new HashMap<>();
        for (TileEffectType effect : values()) {
            typeMap.put(effect.toString().toLowerCase(), effect);
        }
        if (typeMap.containsKey(lowType)) {
            return typeMap.get(lowType);
        }
        throw new IllegalArgumentException("Unknown Effect Type:" + inType);
    }
}
