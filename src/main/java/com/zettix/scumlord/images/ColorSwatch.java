package com.zettix.scumlord.images;

import com.zettix.scumlord.tile.SlumColors;
import com.zettix.scumlord.tile.TileTag;

import java.awt.*;

public class ColorSwatch {

  public static Color getColor(SlumColors inColor) {
      switch (inColor) {
         case GREEN:
            return new Color(0, 200, 0, 255);
         case BLUE:
            return new Color(20, 140, 255, 255);
         case YELLOW:
            return new Color(195, 195, 0, 255);
         case GRAY:
            return new Color(128, 128, 128, 255);
         case OCEAN:
            return new Color(50, 120, 228, 255);
         default:
            return new Color(0, 255, 255, 255);
      }
   }

    public static Color WHITE = new Color(255, 255, 255, 255);
    public static Color CLEAR = new Color(0, 0, 0, 0);

    public static String ColorToGlyph(SlumColors inColor) {
        switch (inColor) {
            case GREEN:
                return "🏠";
            case BLUE:
                return "🏢";
            case YELLOW:
                return "🏭";
            case GRAY:
                return "🏛️";
            case OCEAN:
                return "";
            default:
                return "?";
        }
    }

    public static String PERSON_GLYPH = "👤";
    public static String DEAD_GLYPH = "☠";
    // alt: ♟, ⚑, ☠, ☺, ⛹, ⛄, ⛩ 💀
    /*
    public static String PERSON_GLYPH = "☻";
                return "☖";🏠
                return "☶";📘
                return "⚒";🍔
                return "☕";
                return "🍸";👻🖉
                return "✈";
                return "⛫";🏙
                return "☎";🖿
https://en.wikipedia.org/wiki/Miscellaneous_Symbols_and_Pictographs
     */
    //https://en.wikipedia.org/wiki/Transport_and_Map_Symbols
    //  ⛇https://en.wikipedia.org/wiki/Miscellaneous_Symbols
    public static String TagToGlyph(TileTag inTag) {
        switch (inTag) {
            case OFFICE:
                return "🖿";
            case SCHOOL:
                return "🖉";
            case RESTAURANT:
                return "🍴";
            case AIRPORT:
                return "🛪";
            case DEALERSHIP:
                return "🚗";
            case SKYSCRAPER:
                return "🏙";
            case NONE:
                return "";
            default:
                return "?";
        }
    }
}
