package com.zettix.scumlord.images;

import com.zettix.scumlord.Game;
import com.zettix.scumlord.Player;
import com.zettix.scumlord.PlayerStatChange;
import com.zettix.scumlord.tile.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Statement;
import java.util.List;

import static com.zettix.scumlord.images.ColorSwatch.*;

/// ddddd-dinbats zapf.
// for tags. yay, and free.


public class GenerateTiles {

   public GenerateTiles() {

      Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
      File out = new File("allfonts");
      try {
         Writer writer = new FileWriter(out);
         BufferedWriter bufferedWriter = new BufferedWriter(writer);
         for (Font font : fonts) {
            bufferedWriter.write(font.toString() + "\n");
         }
         bufferedWriter.close();
         writer.close();
      } catch (IOException e) {
         System.out.println("Could not collect font info. " + e.getMessage());
      }
   }
   private void DrawFundsChange(Graphics2D g, int change, int xpos, int ypos, int fontsize) {
      g.drawString("$" + change, xpos, ypos);
   }

   private void DrawIncomeChange(Graphics2D g, int change, int xpos, int ypos, int fontsize) {
       int rad = fontsize * 2;
       String sign = "+";
      int diff = (int) (rad * .25);
       if (change > 0) {
          g.setColor(Color.BLACK);
       } else {
          g.setColor(Color.RED);
          sign = " ";
       }
       int yoff = (int) (fontsize * 1.5);
      g.fillOval(xpos - diff / 2 , ypos - yoff - diff / 2 , rad + diff, rad + diff);
      g.setColor(Color.WHITE);
      rad = fontsize * 2;
      g.fillOval(xpos , ypos  - yoff, rad, rad);
      g.setColor(Color.BLACK);
      g.drawString(sign + change, xpos, ypos);
   }

   private void DrawReputationChange(Graphics2D g, int change, int xpos, int ypos, int fontsize) {
      //xpos -= (fontsize * 0.5);
      int rad = (int) (fontsize * 1.8);
      String sign = "+";
      int diff = (int) (rad * .25);
      if (change > 0) {
         g.setColor(Color.WHITE);
      } else {
         g.setColor(Color.RED);
         sign = " ";
      }
      int yoff = (int) (fontsize * 1.5);
      g.fillRect(xpos - diff / 2 , ypos - yoff - diff / 2 , rad + diff, rad + diff);
      g.setColor(Color.BLACK);
      rad = (int) (fontsize * 1.8);
      g.fillRect(xpos , ypos  - yoff, rad, rad);
      g.setColor(Color.WHITE);
      g.drawString(sign + change, xpos, ypos);
   }

   private void DrawPopulationChange(Graphics2D g, int change, int xpos, int ypos, int fontsize) {
      String sign = "+";
       if (change > 0) {
          g.setColor(WHITE);
       } else {
          g.setColor(Color.RED);
          sign = " ";
       }
      g.drawString(sign + change + PERSON_GLYPH, xpos - fontsize, ypos);
   }

   private void drawBoxes(Graphics2D graphics2D, PlayerStatChange change, int fontsize, int xpos, int ypos) {
      if (change.getFundsChange() != 0) {
         DrawFundsChange(graphics2D, change.getFundsChange(), xpos, ypos, fontsize);
         ypos += fontsize * 2;
      }
      if (change.getIncomeChange() != 0) {
         DrawIncomeChange(graphics2D, change.getIncomeChange(), xpos, ypos, fontsize);
         ypos += fontsize * 2;
      }
      if (change.getPopulationChange() != 0) {
         DrawPopulationChange(graphics2D, change.getPopulationChange(), xpos, ypos, fontsize);
         ypos += fontsize * 2;
      }
      if (change.getReputationChange() != 0) {
        DrawReputationChange(graphics2D, change.getReputationChange(), xpos, ypos, fontsize);
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

   private void drawInverseHexagon(Graphics2D graphics2d) {
      // 4 triangles really...
      int[] xpoints = new int[3];
      int[] ypoints = new int[3];
      Polygon polygon = new Polygon(xpoints, ypoints, 6);
      graphics2d.fillPolygon(polygon);
   }

   private void drawTitle(Graphics2D graphics2d, String title, int fontsize) {
      int ypos = 32 + fontsize;
      String[] parts = title.split(" ");
      for (String part : parts) {
         int namelen = part.length() * 3 * fontsize / 7;
         graphics2d.drawString(part, xSize / 2 - namelen - fontsize, ypos);
         ypos += 5 + fontsize;
      }
   }

   private void drawTag(Graphics2D graphics2d, Tile tile, Color color, Font font) {
      graphics2d.setColor(color);
      graphics2d.setFont(font);
      int ypos = (int) (ySize / 2 + ySize * 0.05);
      int xpos = (int) (xSize * 0.62);
      if (tile.getTileTag() != TileTag.NONE) {
         graphics2d.drawString(TagToGlyph(tile.getTileTag()), xpos, ypos);
      }
   }

   private void drawAction(Graphics2D g, TileAction action, int xpos, int ypos, int fontsize) {
      String message = "f";
      if (action.match(TileEffectType.ANY, TileEffectTime.ONGOING, TileAreaEffect.ADJACENT)) {
         message = "for every adjacent";
      }
      if (action.match(TileEffectType.ANY, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL)) {
         message = "for every";
      }
      if (action.match(TileEffectType.ANY, TileEffectTime.ONGOING, TileAreaEffect.PLAYER_GLOBAL)) {
         message = "for every player";
      }
      if (action.match(TileEffectType.ANY, TileEffectTime.AFTER_NEW, TileAreaEffect.GAME_GLOBAL)) {
         message = "for every new";
      }
      if (action.match(TileEffectType.ANY, TileEffectTime.AFTER_NEW, TileAreaEffect.PLAYER_GLOBAL)) {
         message = "for every player new";
      }
      PlayerStatChange change = action.getChange();
      drawBoxes(g, change, fontsize, xpos, ypos) ;
      g.drawString(message, xpos + fontsize * 3, ypos);
   }

   private void drawCost(Graphics2D graphics2d, Tile tile, int fontsize, Font font) {
       int cost = tile.getCost();
       SlumColors color = tile.getColor();
      int ypos = ySize / 2 - (int) (ySize * 0.1);
      int xpos = (int) (xSize * 0.12);
      String s = "$" + cost;
      graphics2d.drawString(s, xpos, ypos);
      // COLOR TAG
      graphics2d.setFont(font);
      ypos += (int) (fontsize * 1.4);
      graphics2d.drawString(ColorToGlyph(color), xpos, ypos);
   }

   public void PaintImages() {
      int fontsize = 16;
      String fontname = "Bitstream Vera Serif";
      String outdir = "src/main/resources/images/";
      Font helvetica = new Font("Helvetica", Font.PLAIN, fontsize + 4);

      System.out.println("Writing tile images.");
      Game game = new Game();
      game.Load();
      for (TileSeries series : TileSeries.values()) {
         List<Tile> tileList = game.getTilesBySeries(series);
         System.out.println("Writing Series " + series + " images[" + tileList.size() + "].");
         for (Tile tile : tileList) {
            BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = image.getGraphics();
            Graphics2D graphics2D = (Graphics2D) graphics;
            Color color = getColor(tile.getColor());
            Font font = new Font(fontname, Font.BOLD, fontsize);
            graphics2D.setFont(font);
            graphics2D.setColor(CLEAR);
            graphics2D.fillRect(0, 0, xSize, ySize);
            graphics2D.setColor(color);
            drawHexagon(graphics2D);
            graphics2D.setBackground(color);
            graphics2D.setColor(WHITE);
            drawTitle(graphics2D, tile.getName().toUpperCase(), fontsize);
            font = new Font(fontname, Font.BOLD, fontsize + 5);
            graphics.setFont(font);
            // COST
            drawCost(graphics2D, tile, fontsize, helvetica);
            drawTag(graphics2D, tile, Color.WHITE , helvetica);

            int ypos = ySize / 2 - (int) (ySize * 0.1);
            int xpos = (int) (xSize * 0.62);

            List<TileAction> actions = tile.getActions();
            for (TileAction action: actions) {
               if (action.match(TileEffectType.ANY, TileEffectTime.INSTANT, TileAreaEffect.ANY)) {
                  drawBoxes(graphics2D, action.getChange(), fontsize, xpos, ypos);
               }
            }

            ypos = ySize - ySize / 6;
            xpos = xSize / 5;
            for (TileAction action: actions) {
               if (!action.match(TileEffectType.ANY, TileEffectTime.INSTANT, TileAreaEffect.ANY)) {
                  drawAction(graphics2D, action, xpos, ypos, fontsize);
                  ypos -= fontsize * 2.5;
               }
            }

            String outname = tile.getName().replace(" ", "_");
            File outputfile = new File(outdir + "Tile_" + series.toString()
                    + "-" + outname + ".png");
            try {
               ImageIO.write(image, "png", outputfile);
            } catch (IOException e) {
               System.err.println("Could not write file!" + e.getMessage());
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
   private int ySize = 221;  // sqrt(3)/2 [0.866] * xSize
}
