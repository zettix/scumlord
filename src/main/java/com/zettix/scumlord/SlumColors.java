package com.zettix.scumlord;

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
}
