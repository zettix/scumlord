package com.zettix.scumlord.images;

import com.zettix.scumlord.Game;
import com.zettix.scumlord.tile.SlumColors;
import com.zettix.scumlord.tile.Tile;
import com.zettix.scumlord.tile.TileSeries;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GenerateTiles {

   public GenerateTiles() {

      Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
      for (Font font: fonts) {
         System.out.println("System Font: " + font.toString());
      }

   }


   private Color getColor(SlumColors inColor) {
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
            return new Color(0, 0, 128, 255);
         default :
            return new Color(0, 255, 255, 255);
      }
   }

   private void drawHexagon(Graphics2D graphics2d) {
      int[] xpoints = new int[6];
      int[] ypoints = new int[6];
      double r = ySize / 2;

      for (int idx = 0; idx < xpoints.length; idx++) {
         double factor = (double) idx / 6.0;
         double theta = Math.PI * 2.0 * factor;
         double x = r * Math.cos(theta);
         double y = r * Math.sin(theta);
         xpoints[idx] = (int) (x + r);
         ypoints[idx] = (int) (y + r);
      }
      Polygon polygon = new Polygon(xpoints, ypoints, 6);
      graphics2d.fillPolygon(polygon);
   }

   private void drawTitle(Graphics2D graphics2d, String title, int fontsize) {
      int ypos = 32 + fontsize;
      String[] parts = title.split(" ");
      for (String part : parts) {
         int namelen = part.length() * 3 * fontsize / 7;
         graphics2d.drawString(part, xSize / 2 - namelen, ypos);
         ypos += 5 + fontsize;
      }
   }

   private void drawCost(Graphics2D graphics2d, int cost, int fontsize) {
      int ypos = ySize / 2 - (int)(ySize * 0.1);
      int xpos = (int) (xSize * 0.12);
      String s = "$" + cost;
      graphics2d.drawString(s, xpos, ypos);
   }

   public void PaintImages() {
      Color WHITE = new Color(255, 255, 255, 255);
      Color CLEAR = new Color(0, 0, 0, 0);
      int fontsize = 18;
      String fontname = "Bitstream Vera Serif";
      String outdir = "src/main/resources/";

      System.out.println("Writing tile images.");
      Game game = new Game();
      game.Load();
      for (TileSeries series : TileSeries.values()) {
         List<Tile> tileList =  game.getTilesBySeries(series);
         System.out.println("Writing Series " + series + " images[" + tileList.size() + "].");
         for (Tile tile : tileList) {
            BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = image.getGraphics();
            Graphics2D graphics2D = (Graphics2D) graphics;
            Color color =  getColor(tile.getColor());
            Font font = new Font(fontname, Font.BOLD, fontsize);
            graphics2D.setFont(font);
            graphics2D.setColor(CLEAR);
            graphics2D.fillRect(0,0, xSize, ySize);
            graphics2D.setColor(color);
            drawHexagon(graphics2D);
            graphics2D.setBackground(color);
            graphics2D.setColor(WHITE);
            drawTitle(graphics2D, tile.getName().toUpperCase(), fontsize);
            font = new Font(fontname, Font.BOLD, fontsize + 5);
            graphics.setFont(font);
            drawCost(graphics2D, tile.getCost(), fontsize);

            String outname = tile.getName().replace(" ", "_");
            File outputfile = new File(outdir + "Tile_" + series.toString()
                    + "-" + outname + ".png");
            try {
               ImageIO.write(image, "png", outputfile);
            } catch (IOException e) {
               System.err.println("Cound not write file!" + e.getMessage());
               return;
            }
         }
      }
   }

   public static void main(String... args) {
      GenerateTiles generateTiles = new GenerateTiles();
      generateTiles.PaintImages();
   }

   private int xSize = 256;
   private int ySize = 256;
}
