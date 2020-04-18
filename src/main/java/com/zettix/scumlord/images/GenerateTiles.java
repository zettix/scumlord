package com.zettix.scumlord.images;

import com.zettix.scumlord.Game;
import com.zettix.scumlord.PlayerStatChange;
import com.zettix.scumlord.tile.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import static com.zettix.scumlord.images.ColorSwatch.*;

public class GenerateTiles {
    private int borderSize = 5;
    private int fontsize = 16;
    private Graphics2D graphics2D;
    public Map<SlumColors, Image> colorTiles;

    public GenerateTiles() {
        colorTiles = new HashMap<>();

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
            for (SlumColors color : SlumColors.values()) {
                String colorname = color.toString();
                String indir = "src/main/resources/images/";
                File infile = new File(indir + "Tag_" + colorname + ".png");
                try {
                    BufferedImage bufferedImage = ImageIO.read(infile);
                    colorTiles.put(color, bufferedImage);
                    System.out.println("Got:" + color + ":" + bufferedImage);
                } catch (IOException f) {
                    System.err.println("Could not read tag file!" + f.getMessage());
                    return;
                }
            }

        System.out.println("Color size:" + colorTiles.size());
    }

    private int DrawFundsChange(int change, int xpos, int ypos) {
        String label = "$" + change;
        graphics2D.drawString(label, xpos, ypos);
        return xpos + (int) (fontsize * 1.6) ;
    }

    private int DrawIncomeChange(int change, int xpos, int ypos) {
        double zoom = 1.5;
        int rad = (int) (fontsize * zoom);
        String sign = "+";
        int diff = (int) (rad * .25);
        if (change > 0) {
            graphics2D.setColor(Color.BLACK);
        } else {
            graphics2D.setColor(Color.RED);
            sign = " ";
        }
        int yoff = (int) (fontsize * 1.5);
        graphics2D.fillOval(xpos - diff / 2 , ypos - yoff - diff / 2 , rad + diff, rad + diff);
        graphics2D.setColor(Color.WHITE);
        rad = (int) (fontsize * zoom);
        graphics2D.fillOval(xpos , ypos  - yoff, rad, rad);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString(sign + change, xpos  , ypos - fontsize / 3 );
        return xpos + (int) (fontsize * 2.0);
    }

    private int DrawReputationChange(int change, int xpos, int ypos) {
        //xpos -= (fontsize * 0.5);
        double zoom = 1.3;
        int rad = (int) (fontsize * zoom);
        String sign = "+";
        int diff = (int) (rad * .25);
        if (change > 0) {
            graphics2D.setColor(Color.WHITE);
        } else {
            graphics2D.setColor(Color.RED);
            sign = " ";
        }
        int yoff = (int) (fontsize * 1.5);
        graphics2D.fillRect(xpos - diff / 2 , ypos - yoff - diff / 2 , rad + diff, rad + diff);
        graphics2D.setColor(Color.BLACK);
        rad = (int) (fontsize * zoom);
        graphics2D.fillRect(xpos , ypos  - yoff, rad, rad);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(sign + change, xpos - fontsize / 8, ypos - fontsize / 2);
        return xpos + (int) (fontsize * 2.0);
    }

    private int DrawPopulationChange(int change, int xpos, int ypos) {
        String sign = "+";
        if (change > 0) {
            graphics2D.setColor(WHITE);
        } else {
            graphics2D.setColor(Color.RED);
            sign = " ";
        }
        String label = sign + change;
        int labelLength = label.length() + 1;
        label += PERSON_GLYPH;
        graphics2D.drawString(label, xpos, ypos);
        return (int) (fontsize * 6.1);
    }

    private int DrawSlash(int xpos, int ypos) {
        graphics2D.setColor(Color.BLACK);
        return xpos;
    }

    private int drawBoxes(PlayerStatChange change, int xpos, int ypos) {
        int changeCount = 0;
        if (change.getFundsChange() != 0) {
            changeCount++;
        }
        if (change.getIncomeChange() != 0) {
            changeCount++;
        }
        if (change.getReputationChange() != 0) {
            changeCount++;
        }
        if (change.getReputationChange() != 0) {
            changeCount++;
        }
        // boolean drawSlash = (changeCount > 1)? true : false;

        if (change.getFundsChange() != 0) {
            xpos = DrawFundsChange(change.getFundsChange(), xpos, ypos);
        }
        if (change.getIncomeChange() != 0) {
            xpos = DrawIncomeChange(change.getIncomeChange(), xpos, ypos);
        }
        if (change.getPopulationChange() != 0) {
            xpos = DrawPopulationChange(change.getPopulationChange(), xpos, ypos);
        }
        if (change.getReputationChange() != 0) {
            xpos = DrawReputationChange(change.getReputationChange(), xpos, ypos);
        }
        return xpos;
    }

    private void drawHexagon() {
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
        graphics2D.fillPolygon(polygon);
        graphics2D.setColor(new Color(62,62,124));
        graphics2D.setStroke(new BasicStroke(borderSize));
        graphics2D.drawPolygon(polygon);
    }

    private void drawInverseHexagon() {
        // 4 triangles really...
        int[] xpoints = new int[3];
        int[] ypoints = new int[3];
        Polygon polygon = new Polygon(xpoints, ypoints, 6);
        graphics2D.fillPolygon(polygon);
    }

    private void DrawRedLine(int x, int y) {
        int h = fontsize;
        int linewidth = 5;
        graphics2D.setColor(Color.RED);
        graphics2D.setStroke(new BasicStroke(linewidth));
        graphics2D.drawLine(x + h / 2, y - h, x + h / 2, y + h);
    }


    private void drawTag(Tile tile, Color color, Font font) {
        graphics2D.setColor(color);
        graphics2D.setFont(font);
        int ypos = (int) (ySize / 2 + ySize * 0.05);
        int xpos = (int) (xSize * 0.8);
        if (tile.getTileTag() != TileTag.NONE) {
            graphics2D.drawString(TagToGlyph(tile.getTileTag()), xpos, ypos);
        }
    }

    private String actionColors(TileAction action) {
        List<String> colorNames = new ArrayList<>();
        for (SlumColors color : action.getFilterColors()) {
            colorNames.add(color.toString());
        }
        String label = "";
        for (int i = 0; i < colorNames.size(); i++) {
            label += colorNames.get(i);
            if (i < colorNames.size() - 1) {
                label += ", ";
            }
        }
        return label;
    }

    private String actionTags(TileAction action) {
        List<String> tagNames = new ArrayList<>();
        for (TileTag tileTag : action.getFilterTags()) {
            tagNames.add(tileTag.toString());
        }
        String label = "";
        for (int i = 0; i < tagNames.size(); i++) {
            label += tagNames.get(i);
            if (i < tagNames.size() - 1) {
                label += ", ";
            }
        }
        return label;
    }

    private void drawColorTile(SlumColors color, int xpos, int ypos) {
        graphics2D.drawImage(colorTiles.get(color), xpos, ypos, null);
    }

    private void drawTagSpot(String glyph, int xpos, int ypos) {
        graphics2D.drawString(glyph, xpos, ypos);
    }

    private void drawSingleAction(TileAction action, int xpos, int ypos, int numactions) {
        String message = "Unknown Action";
        boolean drawTags = false;
        boolean drawColors = false;
        boolean drawRedLine = false;
        boolean underlabel = false;
        int xLabel = xpos;
        int yLabel = ypos;
        int numstuff = 0;
        // Colors
        if (action.match(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.ADJACENT)) {
            message = "for each adjacent "; // + actionColors(action);
            drawColors = true;
            underlabel = true;
            numstuff = action.getFilterColors().size();
        }
        if (action.match(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.PLAYER_GLOBAL)) {
            message = "for each of your "; // + actionColors(action);
            drawColors = true;
        }
        if (action.match(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL)) {
            message = "for every "; // + actionColors(action);
            drawColors = true;
        }
        if (action.match(TileEffectType.COLOR, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL_OTHER)) {
            message = "for all other boroughs'"; // + actionColors(action);
            drawColors = true;
        }
        if (action.match(TileEffectType.COLOR, TileEffectTime.AFTER_NEW, TileAreaEffect.GAME_GLOBAL)) {
            // message = "for all " + actionColors(action) + " built after this one";
            message = "for all " + actionColors(action) + " built after";
            drawColors = true;
        }
        // Tags
        if (action.match(TileEffectType.TAG, TileEffectTime.ONGOING, TileAreaEffect.ADJACENT)) {
            message = "for each adjacent " + actionTags(action);
            drawTags = true;
        }
        if (action.match(TileEffectType.TAG, TileEffectTime.ONGOING, TileAreaEffect.PLAYER_GLOBAL)) {
            message = "for each of your " + actionTags(action);
            drawTags = true;
        }
        if (action.match(TileEffectType.TAG, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL)) {
            message = "for every " + actionTags(action);
            drawTags = true;
        }
        if (action.match(TileEffectType.TAG, TileEffectTime.ONGOING, TileAreaEffect.GAME_GLOBAL_OTHER)) {
            message = "for all other boroughs'" + actionTags(action);
            drawTags = true;
        }
        if (action.match(TileEffectType.TAG, TileEffectTime.AFTER_NEW, TileAreaEffect.GAME_GLOBAL)) {
            //message = "for all " + actionTags(action) + " built after this one";
            message = "for all " + actionTags(action) + " built after";
            drawTags = true;
        }

        // Red Line
        if (action.match(TileEffectType.RED_LINE, TileEffectTime.AFTER_NEW, TileAreaEffect.ANY)) {
            message = "after each";
            drawRedLine = true;
        }

        // Double lake
        if (action.match(TileEffectType.LAKE_DOUBLE, TileEffectTime.ONGOING, TileAreaEffect.ANY)) {
            message = "Lakes pay $2 more";
        }
        PlayerStatChange change = action.getChange();
        if (change != null) {
            xpos = drawBoxes(change, xpos, ypos) ;
        }

        graphics2D.setColor(Color.BLACK);
        List<String> parts = paginate(message, 12);
        int oldy = ypos;
        int offset =  (int) (fontsize * 0.7);
        for (String part : parts) {
            //int namelen = part.length() * 3 * fontsize / 7;
            graphics2D.drawString(part, xpos, ypos - offset);
            ypos += 5 + fontsize;
        }
        xpos = (int) (xSize * 0.7);
        ypos = oldy;
        if (numactions > 1) {
            underlabel = false;
        }
        if (drawColors) {
            if (underlabel) {
                System.out.println("Numstuff: "  + numstuff);
                xpos = xSize / 2 - fontsize * numstuff;
                ypos += fontsize * 2;
            }
            ypos -= fontsize;
            for (SlumColors color : action.getFilterColors()) {
                drawColorTile(color, xpos, ypos);
                xpos += fontsize * 2;
            }
        }
        if (drawTags) {
            for (TileTag tag : action.getFilterTags()) {
                drawTagSpot(TagToGlyph(tag), xpos, ypos);
                xpos += 10;
            }
        }
        if (drawRedLine) {
            DrawRedLine(xpos, ypos);
        }
    }

    private void drawCost(Tile tile, Font font, Font tagFont) {
        int cost = tile.getCost();
        SlumColors color = tile.getColor();
        int ypos = ySize / 2 - (int) (ySize * 0.1);
        int xpos = (int) (xSize * 0.12);
        String s = "$" + cost;
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(font);
        graphics2D.drawString(s, xpos, ypos);
        // COLOR TAG
        graphics2D.setFont(tagFont);
        ypos += (int) ((fontsize + 4) * 1.7);
        graphics2D.drawString(ColorToGlyph(color), xpos, ypos);
    }

    private void clear() {
        graphics2D.setColor(CLEAR);
        graphics2D.fillRect(0, 0, xSize, ySize);
    }

    private List<String> paginate(String string, int maxstring) {
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
                int spacefind = s1.indexOf(" ");
                if ((s1.length() > maxstring) && spacefind > 0) {
                        List<String> stuff = paginate(s1, maxstring);
                        for (String ess : stuff) {
                            result.add(ess);
                        }
                } else {
                    result.add(s1);
                }
                spacefind = s2.indexOf(" ");
                if ((s2.length() > maxstring) && spacefind > 0) {
                        List<String> stuff = paginate(s2, maxstring);
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

    private void drawTitle(Color color, Tile tile, Font font) {
        graphics2D.setColor(color);
        drawHexagon();
        graphics2D.setBackground(color);
        graphics2D.setColor(Color.BLACK);
        int ypos =  16 + fontsize;
        String title = tile.getName().toUpperCase();
        graphics2D.setFont(font);
        List<String> parts = paginate(title, 13);
        for (String part : parts) {
            int namelen = part.length() * 3 * fontsize / 7;
            graphics2D.drawString(part, xSize / 2 - namelen / 2 - fontsize - 4 , ypos);
            ypos += 5 + fontsize;
        }
    }

    public void GenerateOpenSpotTile() {
        BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        graphics2D = (Graphics2D) graphics;
        clear();
        graphics2D.setColor(new Color(220, 190, 200));
        drawHexagon();
        // Write image
        String outname = "Open";
        String outdir = "src/main/resources/images/";
        File outputfile = new File(outdir + "Tile_" + outname + ".png");
        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            System.err.println("Could not write file!" + e.getMessage());
        }

        int[] xpos = { 40, 40, 100, 100, 70};
        int[] ypos = { 40, 100, 100, 40, 70};
        int i =  0;
        for (SlumColors color : SlumColors.values()) {
            int x = xpos[i];
            int y = ypos[i];
            i++;
            graphics2D.drawImage(colorTiles.get(color), x, y, null);
        }

    }

    public void PaintImages() {
        //String fontname = "Bitstream Vera Serif";
        String fontname = "Helvetica";
        String outdir = "src/main/resources/images/";
        Font helvetica = new Font("Helvetica", Font.BOLD, fontsize + 0);
        Font titleFont = new Font("Helvetica", Font.BOLD, fontsize - 2);
        Font tagFont = new Font("Helvetica", Font.PLAIN, fontsize + 14);

        System.out.println("Writing tile images.");
        Game game = new Game();
        game.Load();
        for (TileSeries series : TileSeries.values()) {
            List<Tile> tileList = game.getTilesBySeries(series);
            System.out.println("Writing Series " + series + " images[" + tileList.size() + "].");
            for (Tile tile : tileList) {
                BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
                Graphics graphics = image.getGraphics();
                graphics2D = (Graphics2D) graphics;
                clear();
                Color color = getColor(tile.getColor());
                drawTitle(color , tile, titleFont);
                Font costFont = new Font("Helvetica", Font.BOLD, fontsize + 4);
                // Draw Cost
                drawCost(tile, costFont, tagFont);
                // Draw Tag, if present.
                drawTag(tile, Color.WHITE , tagFont);


                // Draw Instant.
                graphics2D.setFont(helvetica);
                int ypos = ySize / 2 - (int) (ySize * 0.1);
                int xpos = (int) (xSize * 0.73);

                int numactions = 0;
                List<TileAction> actions = tile.getActions();
                for (TileAction action: actions) {
                    if (action.match(TileEffectType.ANY, TileEffectTime.INSTANT, TileAreaEffect.ANY)) {
                        drawBoxes(action.getChange(), xpos, ypos);
                    } else {
                        numactions++;
                    }
                }

                // Draw Actions
                ypos = (int) (ySize * 0.77);
                xpos =(int)  (xSize * 0.2);
                if (numactions > 1) {
                    System.err.println("Tile:" + tile.toString() + " Has more than 1 action!");
                }
                for (TileAction action: actions) {
                    if (!action.match(TileEffectType.ANY, TileEffectTime.INSTANT, TileAreaEffect.ANY)) {
                        drawSingleAction(action, xpos, ypos, numactions);
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
