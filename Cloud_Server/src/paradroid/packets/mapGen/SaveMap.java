package paradroid.packets.mapGen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author dxf209
 *
 */
public class SaveMap {

    /**
     * Saves a map as a png image 
     * 
     * @param width		The width of the map
     * @param height	The height of the map
     * @param map		The 2D array of nodes
     * @param fname		The filename to save the image as
     */
    public static void writeMap(int width, int height, Node[][] map, String fname) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                img.setRGB(x,y,map[x][y].getTileType().getVal());
            }

        }

        try {
            ImageIO.write(img, "png", new File(fname));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
