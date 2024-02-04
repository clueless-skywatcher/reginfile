package com.github.cluelessskywatcher.reginfile;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;

public class ImageLoadUtils {
    public static final ImageIcon REFRESH_ICON = loadImage("refresh.png");
    public static final ImageIcon UPONELEVEL_ICON = loadImage("uponelevel.png");

    public static ImageIcon loadImage(String fileName) {
        InputStream stream = ImageLoadUtils.class.getClassLoader().getResourceAsStream(fileName);
        try
        {
            if (stream == null)
            {
                throw new Exception("Cannot find file " + fileName);
            }
            return new ImageIcon(ImageIO.read(stream));
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    public static boolean compareIcons(Icon i1, Icon i2) {
        BufferedImage buff1 = convertToBufferedImage(i1);
        BufferedImage buff2 = convertToBufferedImage(i2);

        return compareBufferedImages(buff1, buff2);
    }

    private static boolean compareBufferedImages(BufferedImage buff1, BufferedImage buff2) {
        if (buff1.getWidth() != buff2.getWidth() || buff1.getHeight() != buff2.getHeight()) {
            return false;
        }

        // Compare pixel by pixel
        for (int y = 0; y < buff1.getHeight(); y++) {
            for (int x = 0; x < buff1.getWidth(); x++) {
                if (buff1.getRGB(x, y) != buff2.getRGB(x, y)) {
                    return false;
                }
            }
        }

        // Images are identical
        return true;
    }

    private static BufferedImage convertToBufferedImage(Icon icon) {
        BufferedImage img = new BufferedImage(
            icon.getIconWidth(),
            icon.getIconHeight(),
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics g = img.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();

        return img;
    }
}
