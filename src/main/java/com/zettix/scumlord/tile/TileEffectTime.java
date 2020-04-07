package com.zettix.scumlord.tile;

import java.util.HashMap;
import java.util.Map;

public enum TileEffectTime {
    INSTANT,
    AFTER_NEW,
    ONGOING;

    public static TileEffectTime fromString(String inTime) {
        String lowTime = inTime.toLowerCase();
        Map<String, TileEffectTime> timeMap = new HashMap<>();
        for (TileEffectTime time : values()) {
            timeMap.put(time.toString().toLowerCase(), time);
        }
        if (timeMap.containsKey(lowTime)) {
            return timeMap.get(lowTime);
        }
        throw new IllegalArgumentException("Unknown Effect Time:" + inTime);
    }
}
