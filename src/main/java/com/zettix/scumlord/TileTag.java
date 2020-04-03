package com.zettix.scumlord;


import java.util.HashMap;
import java.util.Map;

public enum TileTag {
    NONE,
    SCHOOL,
    RESTAURANT,
    OFFICE,
    AIRPORT;

    @Override
    public String toString() {
        switch (this) {
            case NONE:
                return "-";
            case RESTAURANT:
                return "Restaurant";
            case SCHOOL:
                return "School";
            case OFFICE:
                return "Office";
            case AIRPORT:
                return "Airport";
            default:
                return "Unknown TileTag:" + this;
        }
    }

    public static TileTag fromString(String inTag) {
        String lowTag = inTag.toLowerCase();
        Map<String, TileTag> typeMap = new HashMap<>();
        for (TileTag tag : values()) {
            typeMap.put(tag.toString().toLowerCase(), tag);
        }
        if (typeMap.containsKey(lowTag)) {
            return typeMap.get(lowTag);
        }
        throw new IllegalArgumentException("Unknown Tag:" + inTag);
    }
}
