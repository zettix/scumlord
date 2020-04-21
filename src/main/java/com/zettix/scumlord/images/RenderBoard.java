package com.zettix.scumlord.images;

import com.zettix.scumlord.Game;
import com.zettix.scumlord.Player;
import com.zettix.scumlord.hexgrid.HexGrid;
import com.zettix.scumlord.hexgrid.HexPosition;
import com.zettix.scumlord.tile.Tile;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.zettix.scumlord.images.ColorSwatch.WHITE;

public class RenderBoard {

    public RenderBoard(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.xSize = 0;
        this.ySize = 0;
    }

    private void WriteFile(String filename, BufferedImage image) {
        try {
            File outputfile = new File(filename + ".png");
            ImageIO.write(image, "png", outputfile);
        } catch (IOException ex) {
            System.err.println("Couldn't save:" + ex.getMessage());
        }
    }

    public void PaintPlayerStats(Graphics2D graphics2D, int fontsize) {
        graphics2D.setColor(WHITE);
        int ygo = 0;
        graphics2D.drawString("Player:" + player.getName(), 0, fontsize * (ygo + 1));
        ygo++;
        graphics2D.drawString("Income:" + player.getIncome(), 0, fontsize * (ygo + 1));
        ygo++;
        graphics2D.drawString("Reputation:" + player.getReputation(), 0, fontsize * (ygo + 1));
        ygo++;
        graphics2D.drawString("Funds:" + player.getFunds(), 0, fontsize * (ygo + 1));
        ygo++;
        graphics2D.drawString("Score:" + player.getScore(), 0, fontsize * (ygo + 1));

    }

    private void PaintOpenPositions(Graphics2D graphics2D, HexGrid board) {
        List<HexPosition> openPositions = board.getOpenPositions();
        File file = game.getOpenTile();
        Image img;
        try {
            img = ImageIO.read(file);
        } catch (IOException ex) {
            System.err.println("Bad time:" + ex.getMessage());
            return;
        }
        graphics2D.setColor(Color.DARK_GRAY);
        for (HexPosition hexPosition : openPositions) {
            int[] xy = hexPosition.toGrid();
            xy[1] = ySize - xy[1] - yoff;  // flip y
            xy[0] = xSize / 2 + xy[0] - xoff / 2; // center
            graphics2D.drawImage(img, xy[0], xy[1], null);
            String label = board.getChoiceByPosition(hexPosition);
            graphics2D.drawString(label, xy[0] + 128, xy[1] + 100);
        }
    }

    public void Render(String filename, int xres, int yres) {

        xSize = xres;
        ySize = yres;

        HexGrid board = player.getBoard();

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
        for (HexPosition position : board.getLocations()) {
            Tile tile = board.getTile(position);
            File file = game.getTileImageFile(tile);
            int[] xy = position.toGrid();
            xy[1] = ySize - xy[1] - yoff;  // flip y
            xy[0] = xSize / 2 + xy[0] - xoff / 2; // center
            BufferedImage img = null;
            try {
                img = ImageIO.read(file);
                graphics2D.drawImage(img, xy[0], xy[1], null);
            } catch (IOException ex) {
                System.err.println("Bad time:" + ex.getMessage());
                return;
            }
        }
        PaintOpenPositions(graphics2D, player.getBoard());
        PaintPlayerStats(graphics2D, fontsize);
        WriteFile(filename, image);
    }

    private Player player;
    private Game game;
    private int yoff = 221;
    private int xoff = 256;

    private int xSize;
    private int ySize;

}
