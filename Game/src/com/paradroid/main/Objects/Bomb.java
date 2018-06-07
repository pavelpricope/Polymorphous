package com.paradroid.main.Objects;

import com.paradroid.Network.packets.PacketPlayerLife;
import com.paradroid.Network.packets.PacketPowerUp;
import com.paradroid.main.Game;
import com.paradroid.main.Handlers.Handler;
import com.paradroid.main.Player.Player;
import com.paradroid.main.PowerUp.*;
import com.paradroid.util.AchievementHandler.Reader;
import com.paradroid.util.physics.Collision;
import com.paradroid.util.physics.IsometricUtil;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;

import java.util.Iterator;

/**
 * @author pxp660
 */
public class Bomb extends GameObject {

    private int width = Game.BLOCK_SIZE;
    private int height = Game.BLOCK_SIZE;
    private int time_to_explode;
    private int explosion_duration = 0;
    private boolean explosion_created = false;
    private boolean perkState;
    private Handler handler;
    private int size;
    private Player player;     // <- the player who places the bomb
    private Explosion[] explosions;
    private Polygon bounds;

    /**
     * @param x         The x coordinate of the bomb
     * @param y         The y coordinate of the bomb
     * @param id        The id of the bomb
     * @param handler   The handler that will update and render tha bomb
     * @param player    The player that puts the bomb
     * @param time      The time until the bomb will explode
     * @param size      The size of the bomb
     * @param perkState The perkState is used in the multi-player mode where the perks are sent from the player who placed the bomb
     */
    public Bomb(int x, int y, ID id, Handler handler, Player player, int time, int size, boolean perkState) {
        super(x, y, id);
        this.handler = handler;
        this.player = player;
        time_to_explode = time;
        this.perkState = perkState;
        this.size = size;
        explosions = new Explosion[4];
        bounds = IsometricUtil.calculateIsometricTile(x, y, width, height);
        createExplosion();
    }

    @Override
    public void update() {
        if (!explosion_created) {
            if (time_to_explode == 0)
                explode();
            else
                time_to_explode--;
        } else if (explosion_duration == 0)
            destroyNearby();
        else
            explosion_duration--;
    }

    @Override
    public void render(GraphicsContext g) {

        g.drawImage(Game.getImg.BOMB(Game.theme), x + 25, y + 20, width / 2, height / 2);        // 20 so the bomb is in the middle of the tile
    }

    @Override
    public Polygon getBounds() {

        return bounds;
    }

    /**
     * Remove the bomb from the handler and then create an
     * explosion considering the objects around the bomb
     */
    public void explode() {
        player.setCBombs();
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (tempObject.getId() == ID.Bomb && tempObject == this) {
                handler.removeObject(tempObject);
            }
        }
        for (Explosion explosion : explosions) {
            handler.addObject(explosion);
        }
        Game.audioManager.playSFX(Game.audioManager.sfx.explosion); //explosion sound
        explosion_duration = 30;
        explosion_created = true;
        destroyNearby();
    }

    private void createExplosion() {
        for (int i = 0; i < explosions.length; i++) {
            explosions[i] = new Explosion(x + 15, y + 20, ID.Explosion, player.getUsername(), i, size, 30);
        }
    }

    /**
     * Loops trough all the objects of the handler,
     * destroys all the boxes and kills all the players in the range of the explosion
     */
    private void destroyNearby() {

        Explosion e1 = explosions[0];
        Explosion e2 = explosions[1];
        Explosion e3 = explosions[2];
        Explosion e4 = explosions[3];

        Reader.increaseBombAchievement();
        Iterator<GameObject> iter = Game.map.getObstacles().iterator();
        while (iter.hasNext()) {
            GameObject box = iter.next();
            if (box instanceof Box) {
                if (Collision.checkPolygonsCollision(box.getBounds(), e1.getBounds()) || Collision.checkPolygonsCollision(box.getBounds(), e2.getBounds()) || Collision.checkPolygonsCollision(box.getBounds(), e3.getBounds()) || Collision.checkPolygonsCollision(box.getBounds(), e4.getBounds())) {
                    Game.getHandler().removeObject(box);
                    Game.getHandler().addObject(new Space(box.getX(), box.getY(), ID.Space));
                    Game.map.getSpaces().add(new Space(box.getX(), box.getY(), ID.Space));

                    iter.remove();
                    if (perkState) {
                        addPowerUp(box.getX(), box.getY());
                    }
                }
            }
        }
        if (perkState) {
            for (int i = 0; i < Game.getPlayersHandler().players.size(); i++) {
                Player cPlayer = Game.getPlayersHandler().players.get(i);
                if (Collision.checkPolygonsCollision(cPlayer.getBounds(), e1.getBounds()) || Collision.checkPolygonsCollision(cPlayer.getBounds(), e2.getBounds()) || Collision.checkPolygonsCollision(cPlayer.getBounds(), e3.getBounds()) || Collision.checkPolygonsCollision(cPlayer.getBounds(), e4.getBounds())) {
                    cPlayer.loseLife();
                    PacketPlayerLife packetPlayerLife = new PacketPlayerLife(player.getUsername(), cPlayer.getUsername(), 1, Game.lobbyID);
                    packetPlayerLife.writeData(Game.socketClient);
                    if (cPlayer.getId() == ID.Bot) {
                        Reader.increaseKillsAchievement();
                    }
                    System.out.println(cPlayer.getUsername() + " has " + cPlayer.getLives() + " lives");


                }
            }
        }
    }

    /**
     * Add a power up at the position of the box destroyed
     *
     * @param x
     * @param y
     */
    private void addPowerUp(int x, int y) {

        PacketPowerUp packetPowerUp;
        switch (random()) {
            case 0:
                break;
            case 1:
                Game.getHandler().addObject(new PowerUpSpeed(x, y, ID.PowerUp));
                packetPowerUp = new PacketPowerUp(getUsername(), x, y, 1, Game.lobbyID);
                packetPowerUp.writeData(Game.socketClient);
                break;
            case 2:
                Game.getHandler().addObject(new PowerUpBomb(x, y, ID.PowerUp));
                packetPowerUp = new PacketPowerUp(getUsername(), x, y, 2, Game.lobbyID);
                packetPowerUp.writeData(Game.socketClient);
                break;
            case 3:
                Game.getHandler().addObject(new PowerUpBombSize(x, y, ID.PowerUp));
                packetPowerUp = new PacketPowerUp(getUsername(), x, y, 3, Game.lobbyID);
                packetPowerUp.writeData(Game.socketClient);
                break;
            case 4:
                Game.getHandler().addObject(new PowerUpLife(x, y, ID.PowerUp));
                packetPowerUp = new PacketPowerUp(getUsername(), x, y, 4, Game.lobbyID);
                packetPowerUp.writeData(Game.socketClient);
                break;
            case 5:
                Game.getHandler().addObject(new PowerUpRandom(x, y, ID.PowerUp));
                packetPowerUp = new PacketPowerUp(getUsername(), x, y, 5, Game.lobbyID);
                packetPowerUp.writeData(Game.socketClient);
                break;
            case 6:
                Game.getHandler().addObject(new PowerUpFreeze(x, y, ID.PowerUp));
                packetPowerUp = new PacketPowerUp(getUsername(), x, y, 6, Game.lobbyID);
                packetPowerUp.writeData(Game.socketClient);
                break;
            case 7:
                Game.getHandler().addObject(new PowerUpLoseLife(x, y, ID.PowerUp));
                packetPowerUp = new PacketPowerUp(getUsername(), x, y, 7, Game.lobbyID);
                packetPowerUp.writeData(Game.socketClient);
                break;
        }

    }

    public int random() {
        double d = Math.random() * 100;
        if (d > 50 && d <= 55) return 1;
        if (d > 55 && d <= 60) return 2;
        if (d > 60 && d <= 65) return 3;
        if (d > 65 && d <= 70) return 4;
        if (d > 70 && d <= 85) return 5;
        if (d > 85 && d <= 95) return 6;
        if (d > 95 && d <= 100) return 7;
        return 0;
    }

    public int getSize() {
        return size;
    }

    public int getTime() {
        return time_to_explode;
    }

    public String getUsername() {
        return player.getUsername();
    }

    public Explosion[] getExplosion() {
        return explosions;
    }


}
