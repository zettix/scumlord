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
import java.util.ArrayList;
import java.util.List;

import static com.zettix.scumlord.images.ColorSwatch.*;

/// ddddd-dinbats zapf.
// for tags. yay, and free.


public class GenerateTiles {
    private int borderSize = 5;

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
        double zoom = 1.5;
        int rad = (int) (fontsize * zoom);
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
        rad = (int) (fontsize * zoom);
        g.fillOval(xpos , ypos  - yoff, rad, rad);
        g.setColor(Color.BLACK);
        g.drawString(sign + change, xpos  , ypos - fontsize / 3 );
    }

    private void DrawReputationChange(Graphics2D g, int change, int xpos, int ypos, int fontsize) {
        //xpos -= (fontsize * 0.5);
        double zoom = 1.3;
        int rad = (int) (fontsize * zoom);
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
        rad = (int) (fontsize * zoom);
        g.fillRect(xpos , ypos  - yoff, rad, rad);
        g.setColor(Color.WHITE);
        g.drawString(sign + change, xpos - fontsize / 8, ypos - fontsize / 2);
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
        double r = xSize / 2;
        double cy = ySize / 2;

        for (int idx = 0; idx < xpoints.length; idx++) {
            double factor = (double) idx / 6.0;
            double theta = Math.PI * 2.0 * factor;
            double x = r * Math.cos(theta);
            double y = r * Math.sin(theta);
            xpoints[idx] = (int) (x + r);
            ypoints[idx] = (int) (y + cy);
        }
        Polygon polygon = new Polygon(xpoints, ypoints, 6);
        graphics2d.fillPolygon(polygon);
        graphics2d.setColor(new Color(62,62,124));
        graphics2d.setStroke(new BasicStroke(borderSize));
        graphics2d.drawPolygon(polygon);
    }

    private void drawInverseHexagon(Graphics2D graphics2d) {
        // 4 triangles really...
        int[] xpoints = new int[3];
        int[] ypoints = new int[3];
        Polygon polygon = new Polygon(xpoints, ypoints, 6);
        graphics2d.fillPolygon(polygon);
    }


    private void drawTag(Graphics2D graphics2d, Tile tile, Color color, Font font) {
        graphics2d.setColor(color);
        graphics2d.setFont(font);
        int ypos = (int) (ySize / 2 + ySize * 0.05);
        int xpos = (int) (xSize * 0.8);
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
        if (action.match(TileEffectType.RED_LINE, TileEffectTime.AFTER_NEW, TileAreaEffect.ANY)) {
            message = "after passing red line";
        }
        if (action.match(TileEffectType.LAKE_DOUBLE, TileEffectTime.AFTER_NEW, TileAreaEffect.ANY)) {
            message = "Lakes pay $2 more";
        }
        PlayerStatChange change = action.getChange();
        if (change != null) {
            drawBoxes(g, change, fontsize, xpos, ypos) ;
        }
        g.drawString(message, xpos + fontsize * 3, ypos);
    }

    private void drawCost(Graphics2D graphics2d, Tile tile, int fontsize, Font font, Font tagFont) {
        int cost = tile.getCost();
        SlumColors color = tile.getColor();
        int ypos = ySize / 2 - (int) (ySize * 0.1);
        int xpos = (int) (xSize * 0.12);
        String s = "$" + cost;
        graphics2d.setColor(Color.WHITE);
        graphics2d.setFont(font);
        graphics2d.drawString(s, xpos, ypos);
        // COLOR TAG
        graphics2d.setFont(tagFont);
        ypos += (int) (fontsize * 1.7);
        graphics2d.drawString(ColorToGlyph(color), xpos, ypos);
    }

    private void clear(Graphics2D graphics2D) {
        graphics2D.setColor(CLEAR);
        graphics2D.fillRect(0, 0, xSize, ySize);
    }

    private List<String> paginate(String string) {
        int maxstring = 13;
        List<String> result = new ArrayList<>();
        List<Integer> spaces;
        if (string.length() <= maxstring) {
            result.add(string);
            return result;
        } else {
            // find spaces
            System.out.println("Paging:" + string);
            spaces = new ArrayList<>();
            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) == ' ') {
                    spaces.add(i);
                }
            }
            if (spaces.size() > 0) {
                int best = -1;
                int mid = string.length() / 2;
                for (int space  : spaces) {
                    if (best == -1) {
                        best = space;
                    } else {
                        int q = space - mid;
                        if (q < 0) q = -q;
                        int besty = best - mid;
                        if (besty < 0) besty = - besty;
                        if (q < besty) {
                            best = space;
                        }
                    }
                }
                String s1 = string.substring(0, best);
                String s2 = string.substring(best);
                if (s1.length() > maxstring) {
                    List<String> stuff = paginate(s1);
                    for (String ess : stuff) {
                        result.add(ess);
                    }
                } else {
                    result.add(s1);
                }
                if (s2.length() > maxstring) {
                    List<String> stuff = paginate(s2);
                    for (String ess : stuff) {
                        result.add(ess);
                    }
                } else {
                    result.add(s2);
                }
            } else {
                // no space found....
                result.add(string);
            }
        }
        return result;
    }

    private void drawTitle(Graphics2D graphics2D, Color color, Tile tile, int fontsize, Font font) {
        graphics2D.setColor(color);
        drawHexagon(graphics2D);
        graphics2D.setBackground(color);
        graphics2D.setColor(Color.BLACK);
        int ypos =  16 + fontsize;
        String title = tile.getName().toUpperCase();
        graphics2D.setFont(font);
        List<String> parts = paginate(title);
        for (String part : parts) {
            int namelen = part.length() * 3 * fontsize / 7;
            graphics2D.drawString(part, xSize / 2 - namelen / 2 - fontsize - 4 , ypos);
            ypos += 5 + fontsize;
        }
    }

    public void GenerateOpenSpotTile() {
        BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        Graphics2D graphics2D = (Graphics2D) graphics;
        clear(graphics2D);
        graphics2D.setColor(new Color(220, 190, 200));
        drawHexagon(graphics2D);
        // Write image
        String outname = "Open";
        String outdir = "src/main/resources/images/";
        File outputfile = new File(outdir + "Tile_" + outname + ".png");
        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            System.err.println("Could not write file!" + e.getMessage());
        }

    }

    public void PaintImages() {
        int fontsize = 16;
        //String fontname = "Bitstream Vera Serif";
        String fontname = "Helvetica";
        String outdir = "src/main/resources/images/";
        Font helvetica = new Font("Helvetica", Font.BOLD, fontsize + 0);
        Font titleFont = new Font("Helvetica", Font.BOLD, fontsize - 2);
        Font tagFont = new Font("Helvetica", Font.BOLD, fontsize + 14);

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
                clear(graphics2D);
                Color color = getColor(tile.getColor());
                drawTitle(graphics2D, color , tile, fontsize, titleFont);
                Font costFont = new Font("Helvetica", Font.BOLD, fontsize + 4);
                // Draw Cost
                drawCost(graphics2D, tile, fontsize + 4, costFont, tagFont);
                // Draw Tag, if present.
                drawTag(graphics2D, tile, Color.WHITE , tagFont);


                // Draw Instant.
                graphics2D.setFont(helvetica);
                int ypos = ySize / 2 - (int) (ySize * 0.1);
                int xpos = (int) (xSize * 0.80);

                List<TileAction> actions = tile.getActions();
                for (TileAction action: actions) {
                    if (action.match(TileEffectType.ANY, TileEffectTime.INSTANT, TileAreaEffect.ANY)) {
                        drawBoxes(graphics2D, action.getChange(), fontsize, xpos, ypos);
                    }
                }

                // Draw Actions
                ypos = ySize - ySize / 6;
                xpos = xSize / 5;
                for (TileAction action: actions) {
                    if (!action.match(TileEffectType.ANY, TileEffectTime.INSTANT, TileAreaEffect.ANY)) {
                        drawAction(graphics2D, action, xpos, ypos, fontsize);
                        ypos -= fontsize * 2.5;
                    }
                }

                // Write image
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
        generateTiles.GenerateOpenSpotTile();
    }

    private int xSize = 256;
    private int ySize = 221;  // sqrt(3)/2 [0.866] * xSize
}
