package com.zettix.scumlord;

import java.util.HashMap;
import java.util.Map;

public enum TileAreaEffect {
    ANY,
    PLAYER_GLOBAL,
    GAME_GLOBAL,
    ADJACENT;

    public static TileAreaEffect fromString(String inArea) {
        String lowEffect = inArea.toLowerCase();
        Map<String, TileAreaEffect> effectMap = new HashMap<>();
        for (TileAreaEffect effect : values()) {
            effectMap.put(effect.toString().toLowerCase(), effect);
        }
        if (effectMap.containsKey(lowEffect)) {
            return effectMap.get(lowEffect);
        }
        throw new IllegalArgumentException("Unknown Effect Area:" + inArea);
    }
}
