package com.zettix.scumlord.images;

import com.zettix.scumlord.Game;
import com.zettix.scumlord.tile.enums.SlumColors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.zettix.scumlord.images.ColorSwatch.*;

// unicode then.https://en.wikipedia.org/wiki/Miscellaneous_Symbols
/*
⚒: hammer and pick: YELLOW.
⚖: scales: GRAY.
⛫: castle, BLUE.
☗ : shogi, GREEN.
✎: lower right pencil: SCHOOL.
✈: airplane: AIRPORTS☖

colors: green, blue, yellow, gray.
tags: school, restaurant, office, airport
*/

public class GenerateTags {


   private void drawHexagon(Graphics2D graphics2D, double percent, Color color) {
      graphics2D.setColor(color);
      int[] xpoints = new int[6];
      int[] ypoints = new int[6];
      double r = ySize / 2;

      for (int idx = 0; idx < xpoints.length; idx++) {
         double factor = (double) idx / 6.0;
         double theta = Math.PI * 2.0 * factor;
         double x = r * Math.cos(theta) * percent;
         double y = r * Math.sin(theta) * percent;
         xpoints[idx] = (int) (x + r);
         ypoints[idx] = (int) (y + r);
      }
      Polygon polygon = new Polygon(xpoints, ypoints, 6);
      graphics2D.fillPolygon(polygon);

   }

  public void PaintImages() {
      int fontsize = 13;
      String fontname = "Helvetica";
      String outdir = "src/main/resources/images/";

      System.out.println("Writing tile tag images.");
      Game game = new Game();
      game.Load();
      for (SlumColors slumColor : SlumColors.values()) {
          Color color = getColor(slumColor);
         System.out.println("Writing color tag " + slumColor + " image.");
         BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
         Graphics graphics = image.getGraphics();
         Graphics2D graphics2D = (Graphics2D) graphics;
         Font font = new Font(fontname, Font.PLAIN, fontsize);
         graphics2D.setFont(font);
         graphics2D.setColor(CLEAR);
         graphics2D.fillRect(0, 0, xSize, ySize);
         drawHexagon(graphics2D, 1.0, Color.BLACK);
         drawHexagon(graphics2D, .80, color);
         int xpos = (int) (xSize * 0.24);
         int ypos = (int) (ySize * 0.70);
          graphics2D.setColor(Color.WHITE);
         graphics2D.drawString(ColorToGlyph(slumColor),xpos, ypos);
         String outname = slumColor.toString();
            File outputfile = new File(outdir + "Tag_" + outname + ".png");
            try {
               ImageIO.write(image, "png", outputfile);
            } catch (IOException e) {
               System.err.println("Could not write tag file!" + e.getMessage());
               return;
            }
         }
   }

   public static void main(String... args) {
      GenerateTags gen= new GenerateTags();
      gen.PaintImages();
   }

   private int xSize = 40;
   private int ySize = 35;  // sqrt(3)/2 [0.866] * xSize
    private Map<String, String> nameToUnicode;
}
