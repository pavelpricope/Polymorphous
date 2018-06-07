package com.polymorphous.main.Player;

import com.polymorphous.Network.packets.PacketBomb;
import com.polymorphous.Network.packets.PacketMove;
import com.polymorphous.main.*;
import com.polymorphous.main.Handlers.Handler;
import com.polymorphous.main.Handlers.KeyInput;
import com.polymorphous.main.Objects.*;
import com.polymorphous.util.physics.Collision;
import com.polymorphous.main.State;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * @author pxp660
 */

public class Player extends Mob {

    private Handler handler;
    private KeyInput input;
    protected double width = Game.BLOCK_SIZE / 2;
    protected double height = Game.BLOCK_SIZE / 2;
    private Polygon bounds;
    protected int speed = 3;
    private boolean game_paused = false;
    protected int max_bombs = 1;
    private int time_error = 0;
    private boolean frozen = false;
    private int frozen_time = 0;
    protected int c_bombs = 0;             // the current number of bomb placed on the map
    private int escape_time = 0;
    protected int direction = 0;
    protected int bomb_time = 0;          // time between bombs
    private String username;
    private int bomb_size = 1;
    protected int lives = 2;
    private int score = 0;
    private int time_b_p;
    protected boolean isMoving = false;

    /**
     * @param x        The current x position of the player
     * @param y        The current y position of the player
     * @param id       The id of the player
     * @param handler  The handler that updates and renders the player
     * @param input    The input handler used to move tha player
     * @param username The username of the player
     */
    public Player(double x, double y, ID id, Handler handler, KeyInput input, String username) {
        super(x, y, id);
        this.x = x;
        this.y = y;
        this.handler = handler;
        this.input = input;
        this.username = username;
    }


    @Override
    public void update() {

        checkIfFrozen();
        move();

        if (Game.state == State.Multiplayer) {
            PacketMove packet = new PacketMove(this.getUsername(), (int) Math.round(this.x), (int) Math.round(this.y), direction, isMoving, Game.lobbyID);
            packet.writeData(Game.socketClient);
        }

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
                    g.drawImage(Game.getImg.CHAR_DOWN_G(Game.char_no), x - width, y - height * 1.5, width * 3, height * 3);
                else
                    g.drawImage(Game.getImg.CHAR_DOWN(Game.char_no), x - width, y - height * 1.5, width * 3, height * 3);
                break;
            case 1:
                if (isMoving)
                    g.drawImage(Game.getImg.CHAR_RIGHT_G(Game.char_no), x - width, y - height * 1.5, width * 3, height * 3);
                else
                    g.drawImage(Game.getImg.CHAR_RIGHT(Game.char_no), x - width, y - height * 1.5, width * 3, height * 3);
                break;
            case 2:
                if (isMoving)
                    g.drawImage(Game.getImg.CHAR_UP_G(Game.char_no), x - width, y - height * 1.5, width * 3, height * 3);
                else
                    g.drawImage(Game.getImg.CHAR_UP(Game.char_no), x - width, y - height * 1.5, width * 3, height * 3);
                break;
            case 3:
                if (isMoving)
                    g.drawImage(Game.getImg.CHAR_LEFT_G(Game.char_no), x - width, y - height * 1.5, width * 3, height * 3);
                else
                    g.drawImage(Game.getImg.CHAR_LEFT(Game.char_no), x - width, y - height * 1.5, width * 3, height * 3);
                break;
        }

    }


    public void move() {

        handleKeyInput();

        x += velX;
        y += velY;

        if (calculateCollision() > 0) {

            x -= velX;
            y -= velY;
        }


    }

    /**
     * Sets VelX and VelY when a key is pressed and then
     * update the position of the player when the move method is called
     */

    private void handleKeyInput() {
        if (input != null) {
            isMoving = false;
            setVelX(0);
            setVelY(0);
            if (input.esc) {
                game_paused = !game_paused;
            }
            if (!game_paused) {
                if (input.space) {
                    dropBomb();
                }
                if (input.up) {
                    isMoving = true;
                    direction = 2;
                    move_up();
                } else if (input.down) {
                    isMoving = true;
                    direction = 0;
                    move_down();
                } else if (input.left) {
                    isMoving = true;
                    direction = 3;
                    move_left();
                } else if (input.right) {
                    isMoving = true;
                    direction = 1;
                    move_right();
                }
            }
        }


    }

    private void move_up() {
        setVelX(speed);
        setVelY(-speed * 0.5);
        x += velX;
        y += velY;
        if (calculateCollision() == 2 || calculateCollision() == 1) {
            x -= velX;
            y -= velY;
            setVelX(-speed);
            setVelY(-speed * 0.5);
        } else {
            x -= velX;
            y -= velY;
        }
    }

    private void move_down() {
        setVelX(-speed);
        setVelY(speed * 0.5);
        x += velX;
        y += velY;
        if (calculateCollision() == 4 || calculateCollision() == 1 || calculateCollision() == 3) {
            x -= velX;
            y -= velY;
            setVelX(speed);
            setVelY(speed * 0.5);
        } else {
            x -= velX;
            y -= velY;
        }
    }

    private void move_left() {
        setVelX(-speed);
        setVelY(-speed * 0.5);
        x += velX;
        y += velY;
        if (calculateCollision() == 1) {
            x -= velX;
            y -= velY;
            setVelX(-speed);
            setVelY(speed * 0.5);
        } else {
            x -= velX;
            y -= velY;
        }
    }

    private void move_right() {
        setVelX(speed);
        setVelY(speed * 0.5);
        x += velX;
        y += velY;
        if (calculateCollision() == 3 || calculateCollision() == 5) {
            x -= velX;
            y -= velY;
            setVelX(speed);
            setVelY(-speed * 0.5);
        } else if (calculateCollision() == 2) {
            x -= velX;
            y -= velY;
            setVelX(-speed);
            setVelY(speed * 0.5);
        } else {
            x -= velX;
            y -= velY;
        }
    }

    /**
     * @return calculates collision and if the returned
     * integer is greater than 0 the player has collided
     */
    public int calculateCollision() {

        int collision = Collision.checkCollision(getBounds(), ID.Wall) + Collision.checkCollision(getBounds(), ID.Box) /*+ Collision.checkCollision(getBounds(), ID.Bomb)*/;

        return collision;
    }

    protected void dropBomb() {
        if (time_error == 0) {
            if (c_bombs < max_bombs) {
                placeBomb();
                time_error = 7;
            }
        } else {
            time_error--;
        }
    }


    protected void placeBomb() {

        Space space = Game.map.getSpaceAt((int) Math.round(getBounds().getPoints().get(0) + 10), (int) Math.round(getBounds().getPoints().get(1) + 10));
        if (space != null) {

            int xb = space.getX();
            int yb = space.getY();

            c_bombs++;


            escape_time = 40;
            handler.addObject(new Bomb(xb, yb, ID.Bomb, handler, this, 180, bomb_size, true));

            if (Game.state == State.Multiplayer) {
                PacketBomb bomb_packet = new PacketBomb(this.getUsername(), xb, yb, Game.lobbyID);
                bomb_packet.writeData(Game.socketClient);
            }

        }
    }

    @Override
    public Polygon getBounds() {
        bounds = new Polygon();
        double sx = x + width / 2 + 5;
        double sy = y + Game.BLOCK_SIZE / 9;
        //right
        double sw = x + width + 5;
        double sh = y + height / 2;
        //down
        double dx = x + width / 2;
        double dy = y + height - Game.BLOCK_SIZE / 10;
        //left
        double dw = x;
        double dh = y + height / 2 + 1;

        bounds.getPoints().addAll(sx, sy, sw, sh, dx, dy, dw, dh);

        return bounds;
    }

    protected void checkIfFrozen() {
        if (frozen) {
            speed = 1;
            if (frozen_time == 0) {
                speed = 3;
                frozen = false;
            } else
                frozen_time--;
        }
    }

    public void freeze() {
        frozen = true;
        frozen_time = 240;
    }

    public int getLives() {
        return lives;
    }

    public void loseLife() {
        lives = lives - 1;
    }

    public void gainLife() {
        lives++;
        if (lives > 5)
            lives = 5;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public int getBombSize() {
        return bomb_size;
    }

    public void increaseScore(int score) {
        this.score = this.score + score;
    }

    public void increaseBombSize() {
        bomb_size++;
        if (bomb_size > 4)
            bomb_size = 4;

    }

    public void increaseSpeed() {
        speed++;
        if (speed > 5)
            speed = 5;
    }

    public void increaseBombNo() {
        max_bombs++;
        if (max_bombs > 4)
            max_bombs = 4;
    }

    /**
     * sets the value of the current number of bombs placed
     */
    public void setCBombs() {
        c_bombs--;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean getFrozen() {
        return frozen;
    }

    public int getMax_bombs() {
        return max_bombs;
    }

    public void setMovement(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

}
