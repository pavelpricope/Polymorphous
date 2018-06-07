package com.polymorphous.main.Handlers;


import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * @author pxp660
 */

public class KeyInput implements EventHandler<KeyEvent> {

    public boolean up, down, left, right, space, esc;

    @Override
    public void handle(KeyEvent event) {

        if (event.getEventType() == KeyEvent.KEY_PRESSED) {


            switch (event.getCode()) {
                case W:
                    up = true;
                    break;
                case S:
                    down = true;
                    break;
                case D:
                    right = true;
                    break;
                case A:
                    left = true;
                    break;
                case SPACE:
                    space = true;
                    break;

                default:

            }

        } else {

            switch (event.getCode()) {
                case W:
                    up = false;
                    break;
                case S:
                    down = false;
                    break;
                case D:
                    right = false;
                    break;
                case A:
                    left = false;
                    break;
                case SPACE:
                    space = false;
                    break;
                case P:
                    esc = true;
                default:
            }


        }
    }
}

