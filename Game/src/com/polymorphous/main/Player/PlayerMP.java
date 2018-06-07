package com.polymorphous.main.Player;

import com.polymorphous.main.Game;
import com.polymorphous.main.Objects.ID;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.net.InetAddress;

/**
 * @author pxp660
 */
public class PlayerMP extends Player {

    private int skin;
    private InetAddress ipAddress;
    private int port;


    public PlayerMP(double x, double y, ID id, String username, int skin, InetAddress ipAddress, int port) {
        super(x, y, id, null, null, username);
        this.x = x;
        this.y = y;
        this.skin = skin;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void update() {
    }


    @Override
    public void render(GraphicsContext g) {

        //up
        double sx = x + width / 2;
        double sy = y + Game.BLOCK_SIZE / 8;
        //right
        double sw = x + width;
        double sh = y + height / 2 + 1;
        //down
        double dx = x + width / 2;
        double dy = y + height - Game.BLOCK_SIZE / 10;
        //left
        double dw = x;
        double dh = y + height / 2 + 1;


        double[] xPoints = {sx, sw, dx, dw};
        double[] yPoints = {sy, sh, dy, dh};
        g.setFill(Color.rgb(0, 0, 0, 0.5));
        g.fillPolygon(xPoints, yPoints, 4);


        switch (direction) {
            case 0:
                if (isMoving)
                    g.drawImage(Game.getImg.CHAR_DOWN_G(skin), x - width, y - height * 1.5, width * 3, height * 3);
                else
                    g.drawImage(Game.getImg.CHAR_DOWN(skin), x - width, y - height * 1.5, width * 3, height * 3);
                break;
            case 1:
                if (isMoving)
                    g.drawImage(Game.getImg.CHAR_RIGHT_G(skin), x - width, y - height * 1.5, width * 3, height * 3);
                else
                    g.drawImage(Game.getImg.CHAR_RIGHT(skin), x - width, y - height * 1.5, width * 3, height * 3);
                break;
            case 2:
                if (isMoving)
                    g.drawImage(Game.getImg.CHAR_UP_G(skin), x - width, y - height * 1.5, width * 3, height * 3);
                else
                    g.drawImage(Game.getImg.CHAR_UP(skin), x - width, y - height * 1.5, width * 3, height * 3);
                break;
            case 3:
                if (isMoving)
                    g.drawImage(Game.getImg.CHAR_LEFT_G(skin), x - width, y - height * 1.5, width * 3, height * 3);
                else
                    g.drawImage(Game.getImg.CHAR_DOWN(skin), x - width, y - height * 1.5, width * 3, height * 3);
                break;
        }

    }


}
