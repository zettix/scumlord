package com.zettix.scumlord.images;

import com.zettix.scumlord.Game;
import com.zettix.scumlord.hexgrid.HexGrid;
import com.zettix.scumlord.hexgrid.HexPosition;
import com.zettix.scumlord.tile.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import static com.zettix.scumlord.images.ColorSwatch.CLEAR;
import static com.zettix.scumlord.images.ColorSwatch.WHITE;

public class RenderBoard {

    public RenderBoard(HexGrid board, Game game, int xSize, int ySize) {
        this.board = board;
        this.game = game;
        this.xSize = xSize;
        this.ySize = ySize;

    }

    public void Render(String filename) {

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
        graphics2D.setColor(WHITE);
        graphics2D.drawString("Hello!", 0, 0);
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

        List<HexPosition> openPositions = board.getOpenPositions();
        File file = game.getOpenTile();
        Image img;
        try {
            img = ImageIO.read(file);
        } catch (IOException ex) {
            System.err.println("Bad time:" + ex.getMessage());
            return;
        }
        for (HexPosition hexPosition : openPositions) {
            int[] xy = hexPosition.toGrid();
            xy[1] = ySize - xy[1] - yoff;  // flip y
            xy[0] = xSize / 2 + xy[0] - xoff / 2; // center
            graphics2D.drawImage(img, xy[0], xy[1], null);
        }

        try {
            File outputfile = new File(filename + ".png");
            ImageIO.write(image, "png", outputfile);
        } catch (IOException ex) {
            System.err.println("Couldn't save:" + ex.getMessage());
        }
    }

    private HexGrid board;
    private Game game;
    private int yoff = 221;
    private int xoff = 256;

    private int xSize;
    private int ySize;

}
