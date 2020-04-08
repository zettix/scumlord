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

   }

   private Color getColor(SlumColors inColor) {
      switch (inColor) {
         case GREEN:
            return new Color(0, 200, 0, 200);
         case BLUE:
            return new Color(20, 140, 255, 155);
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

   public void PaintImages() {
      Color WHITE = new Color(255, 255, 255, 255);
      int fontsize = 10;
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
            Font font = new Font("Helvetica", Font.BOLD, fontsize);
            graphics2D.setFont(font);
            graphics2D.setColor(color);
            graphics2D.fillRect(0,0, xSize, ySize);
            graphics2D.setBackground(color);
            graphics2D.setColor(WHITE);
            int namelen = tile.getName().length() * fontsize  / 3;
            graphics2D.drawString(tile.getName(), xSize / 2 - namelen, 30);
            //graphics2D.drawString(tile.getName(), 10, 10);

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

   private int xSize = 100;
   private int ySize = 100;
}
