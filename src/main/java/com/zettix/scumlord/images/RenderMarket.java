package com.zettix.scumlord.images;

import com.zettix.scumlord.Game;
import com.zettix.scumlord.Market;
import com.zettix.scumlord.tile.Tile;
import com.zettix.scumlord.tile.enums.TileSeries;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RenderMarket {


    public RenderMarket(Game game) {
        this.game = game;
    }

    private void WriteFile(String filename, BufferedImage image) {
        try {
            File outputfile = new File(filename + ".png");
            ImageIO.write(image, "png", outputfile);
        } catch (IOException ex) {
            System.err.println("Couldn't save:" + ex.getMessage());
        }
    }


    public void Render(String filename, int xSize, int ySize) {
        BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        Graphics2D graphics2D = (Graphics2D) graphics;
        Color color = Color.BLACK;
        String fontname = "Helvetica";
        int fontsize = 20;
        Font font = new Font(fontname, Font.BOLD, fontsize);
        graphics2D.setFont(font);
        graphics2D.setColor(color);
        graphics2D.fillRect(0, 0, xSize, ySize);
        graphics2D.setBackground(color);
        graphics2D.setColor(Color.WHITE);
        String choices = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("Tax", 0, 220 + fontsize);
        graphics2D.setColor(Color.GREEN);
        graphics2D.drawString("Buy" , 0, 223 + fontsize * 2);
        graphics2D.setColor(Color.BLUE);
        graphics2D.drawString("Lake", 0, 226 + fontsize * 3);

        java.util.List<Tile> tileList = game.getMarket().getCounterTop();
        int tileXsize = 256;
        int tileXorg = 0;
        int tileYorg = 0;
        int count = 0;
        for (Tile tile : tileList) {
            File file = game.getTileImageFile(tile);
            int x = xSize - (count * tileXsize + tileXorg);
            int y = tileYorg;
            BufferedImage img = null;
            try {
                img = ImageIO.read(file);
                graphics2D.drawImage(img, x - tileXsize, y, null);
                System.err.println("Render:" + tile.getName());
            } catch (IOException ex) {
                System.err.println("Bad time:" + ex.getMessage());
                return;
            }
            int tax = 0;
            if (count > 1) {
                tax = (count - 1) * 2;
            }
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawString("" + tax, x - tileXsize / 2 - fontsize, 220 + fontsize);
            graphics2D.setColor(Color.GREEN);
            graphics2D.drawString("" + choices.charAt(count), x - tileXsize / 2 - fontsize, 223 + fontsize * 2);
           Color lightBlue = new Color(130, 200, 255);
            graphics2D.setColor(lightBlue);
            graphics2D.drawString("" + choices.charAt(count + 10), x - tileXsize / 2 - fontsize, 226 + fontsize * 3);
            count++;
        }


        count = 0;
        Map<Tile, Integer> counts = game.getMarket().getStartTileCounts();
        List<String> names = new ArrayList<String>();
        for (Tile t : counts.keySet()) {
            names.add(t.getName());
        }
        Collections.sort(names);
        for (String name : names) {
            Tile tile = game.getTileByName(name);
            if (counts.get(tile) > 0) {
                File file = game.getTileImageFile(tile);
                int x = xSize - (count * tileXsize + tileXorg);
                int y = tileYorg + 300;
                BufferedImage img = null;
                try {
                    img = ImageIO.read(file);
                    graphics2D.drawImage(img, x - tileXsize, y, null);
                    System.err.println("Render:" + tile.getName());
                } catch (IOException ex) {
                    System.err.println("Bad time:" + ex.getMessage());
                    return;
                }
                graphics2D.setColor(Color.GREEN);
                graphics2D.drawString("" + choices.charAt(choices.length() - count - 1), x - tileXsize / 2, 550);
            }
            count++;
        }

        //String fontname1 = "Free Schoolbook Bold Italic";
        //String fontname1 = "Kenyan Coffee";
        String fontname1 = "BPG Excelsior Caps GPL&GNU";

        int fontsize1 = 200;
        Font font1 = new Font(fontname1, Font.PLAIN, fontsize1);
        graphics.setFont(font1);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("Market", 10, ySize - 10);


        WriteFile(filename, image);

    }

    private final Game game;
}
