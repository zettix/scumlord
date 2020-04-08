package com.zettix.scumlord.images;

import com.zettix.scumlord.Game;
import com.zettix.scumlord.tile.SlumColors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.zettix.scumlord.images.ColorSwatch.*;

public class FontSwatch {

    public void PaintImages() {
        String[] fonts = {"Helvetica",
                "Noto Sans Symbols"
        };
        int fontsize = 24;
        int xSize = (2 * fontsize + 6) * 26 + 60;
        int ySize = 200 * fonts.length;
        String outdir = "src/main/resources/images/";

        System.out.println("Writing font swatch.");

        BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, xSize, ySize);

        int fontcount = -1;
        for (String fontname : fonts) {
            fontcount++;
            Font font = new Font(fontname, Font.PLAIN, fontsize);
            graphics2D.setColor(Color.WHITE);
            graphics2D.setFont(new Font("Helvetica", Font.PLAIN, fontsize));
            graphics2D.drawString(fontname, 10, 30 + (200 * fontcount));
            graphics2D.setFont(font);
            String alpha = "abcdefghijklmnopqustuvwxyz";
            String upper = "abcdefghijklmnopqustuvwxyz".toUpperCase();
            for (int i = 0; i < alpha.length(); i++) {
                graphics2D.drawString("" + i + ":", 10 + i * (2 * fontsize + 6), 70 + (200 * fontcount));
                graphics2D.drawString("" + alpha.charAt(i), 10 + i * (2 * fontsize + 6), 100 +(200 * fontcount));
                graphics2D.drawString("" + upper.charAt(i), 10 + i * (2 * fontsize + 6), 130 +(200 * fontcount));
            }
        }
        File outputfile = new File(outdir + "font.png");
        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            System.err.println("Could not write font file!" + e.getMessage());
            return;
        }
    }

   public static void main(String... args) {
      FontSwatch gen= new FontSwatch();
      gen.PaintImages();
   }
}
