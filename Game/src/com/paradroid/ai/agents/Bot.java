package com.paradroid.ai.agents;

import com.paradroid.main.Game;
import com.paradroid.main.Handlers.Handler;
import com.paradroid.main.Objects.ID;
import com.paradroid.main.Player.Player;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author dxf209
 */
public class Bot extends Player {

    private int lives = 3;
    private int score = 0;
    private final Arbiter arbiter;

    private int height = Game.BLOCK_SIZE / 2;
    private int width = Game.BLOCK_SIZE / 2;

    private boolean colliding = false;


    private LinkedList<Boolean> collisionList;
    private Action prevAct = Action.STAY;

    private LinkedList<Action> actionList = new LinkedList<>();

    private boolean isMoving;
    private boolean debug;

    private final int DELAY;
    private int del = 0;

    private boolean active;

    /**
     * Creates a new Bot 
     * 
     * @param x			the bot x position
     * @param y			the bot y position
     * @param id		the id of the bot
     * @param handler	the game handler
     * @param username	the bot username
     * @param active	a boolean value telling us if the bot is active
     * @param debug		a boolean flag for debug
     */
    public Bot(double x, double y, ID id, Handler handler, String username, boolean active, boolean debug) {
        super(x, y, id, handler, null, username);
        arbiter = new Arbiter(0, new Point((int) Math.round(x) + 10, (int) Math.round(y) + 10), bomb_time, this, debug, actionList);
        this.active = active;
        this.debug = debug;
        DELAY = 30; // Was 25
        collisionList = new LinkedList<>();
    }

    @Override
    public void update() {


        arbiter.updateBotPos(new Point((int) Math.round(x), (int) Math.round(y)));
        bomb_time--;
        arbiter.updateBombTime(bomb_time);
        executeAction();
        checkIfFrozen();
        x += velX;
        y += velY;

        if (calculateCollision() > 0) {
            colliding = true;
            if (debug) {
                System.out.println("Colliding!");
            }
            x -= velX;
            y -= velY;
        }
        x = Game.clamp(x, Game.BLOCK_SIZE, Game.WIDTH - Game.BLOCK_SIZE * 2);
        y = Game.clamp(y, Game.BLOCK_SIZE, Game.HEIGHT - Game.BLOCK_SIZE * 2);


    }

    /**
     * Takes in an Action a, that is used to make the bot act in the specified way
     *
     * @param a The action that is to be performed by the bot
     */
    private void executeAction() {
        Action a = arbiter.action(new Point((int) Math.round(x), (int) Math.round(y)), bomb_time);
        if (!active) {
            // Used to pause bot
            setVelX(0);
            setVelY(0);
            return;
        }
        setVelX(0);
        setVelY(0);

        // The delay allows for smooth(ish) movement across the game board
        if (del <= DELAY) {
            a = prevAct;
            del++;
        } else {
            del = 0;
        }

        // The code referencing collision allow the bot to know if it's colliding too much and alter the path if it is
        if (colliding) {
            collisionList.add(true);
        }

        if (collisionList.size() > 20) {
            collisionList = new LinkedList<>();
            Random r = new Random();
            int v = r.nextInt(3);

            switch (prevAct) {
                case UP:
                    if (v == 0) {
                        a = Action.DOWN;
                    } else if (v == 1) {
                        a = Action.LEFT;
                    } else {
                        a = Action.RIGHT;
                    }
                    break;
                case DOWN:
                    if (v == 0) {
                        a = Action.UP;
                    } else if (v == 1) {
                        a = Action.LEFT;
                    } else {
                        a = Action.RIGHT;
                    }
                    break;
                case LEFT:
                    if (v == 0) {
                        a = Action.DOWN;
                    } else if (v == 1) {
                        a = Action.UP;
                    } else {
                        a = Action.RIGHT;
                    }
                    break;
                case RIGHT:
                    if (v == 0) {
                        a = Action.DOWN;
                    } else if (v == 1) {
                        a = Action.LEFT;
                    } else {
                        a = Action.UP;
                    }
                    break;
                default:
                    break;
            }
        }

        switch (a) {
            case UP:
                isMoving = true;
                actionList.add(Action.UP);
                prevAct = Action.UP;
                if (debug) {
                    System.out.println("BOT: Up");
                }
                direction = 2;
                setVelY(-speed / 2);
                setVelX(speed);
                break;
            case DOWN:
                isMoving = true;
                actionList.add(Action.DOWN);
                prevAct = Action.DOWN;
                if (debug) {
                    System.out.println("BOT: Down");
                }
                direction = 0;
                setVelY(speed / 2);
                setVelX(-speed);
                break;
            case LEFT:
                isMoving = true;
                actionList.add(Action.LEFT);
                prevAct = Action.LEFT;
                if (debug) {
                    System.out.println("BOT: Left");
                }
                direction = 3;
                setVelX(-speed);
                setVelY(-speed / 2);
                break;
            case RIGHT:
                isMoving = true;
                actionList.add(Action.RIGHT);
                prevAct = Action.RIGHT;
                if (debug) {
                    System.out.println("BOT: Right");
                }
                direction = 1;
                setVelX(speed);
                setVelY(speed / 2);
                break;
            case STAY:
                prevAct = Action.STAY;
                isMoving = false;
                if (debug) {
                    System.out.println("BOT: Stay");
                }
                break;
            case DROP_BOMB:
                prevAct = Action.DROP_BOMB;
                if (debug) {
                    System.out.println("BOT: Bomb drop");
                }
                dropBomb();
                break;
            default:
                if (debug) {
                    System.out.println("BOT: Default");
                }
                break;
        }
    }


    @Override
    public void render(GraphicsContext g) {


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
                    g.drawImage(Game.getImg.CHAR_DOWN(Game.char_no), x - width, y - height * 1.5, width * 3, height * 3);
                break;
        }


    }


    public int getLives() {
        return lives;
    }

    public void loseLife() {
        lives = lives - 1;
    }

    public void gainLife() {
        lives = lives + 1;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int score) {
        this.score = this.score + score;
    }


    /**
     * Allows the bot to be deactivated, which allows pausing
     *
     * @param active Boolean to set the active-ness of the bot
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public int getBombCount() {
        return max_bombs;
    }

    public int getDeployedBombs() {
        return c_bombs;
    }

    public boolean getCollisionStatus() {
        return colliding;
    }

}
