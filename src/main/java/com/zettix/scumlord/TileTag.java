package com.zettix.scumlord;

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
}
