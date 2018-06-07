package com.paradroid.util.menuHandler;

import com.paradroid.main.Game;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public enum FlippingButton {
    /**
     * @author jxv603
     */

    musicButton(UIobjectsAssets.buttonMusicOn.img, UIobjectsAssets.buttonMusicOff.img, () -> stateMusic(), () -> stateMusic(),true),
    testButton(UIobjectsAssets.buttonMusicOn.img, UIobjectsAssets.buttonMusicOff.img,()-> System.out.println(),()-> System.out.println(),false),
    fxButton(UIobjectsAssets.buttonSoundFxOn.img, UIobjectsAssets.buttonSoundFxOff.img, () -> stateSoundFx(), () -> stateSoundFx(),true);

    private static final double PIE = Math.PI;
    private static final double HALF_PI = Math.PI / 2;
    private static final double ANIMATION_DURATION = 5000;
    private static final double ANIMATION_RATE = 10;

    private final ImageView iv1;
    private final ImageView iv2;
    private final SimpleDoubleProperty angle;
    private final PerspectiveTransform transform = new PerspectiveTransform();
    private final SimpleBooleanProperty flippedProperty = new SimpleBooleanProperty(true);
    public final StackPane flipPane;

    private Timeline animation;

    /**
     * This is the constructor for a flipping button object
     * @param NORMAL_IMAGE
     * @param PRESSED_IMAGE
     * @param frontCase
     * @param backCase
     */
    FlippingButton(Image NORMAL_IMAGE, Image PRESSED_IMAGE, Runnable frontCase, Runnable backCase,boolean b) {
        iv1 = new ImageView(NORMAL_IMAGE);
        iv2 = new ImageView(PRESSED_IMAGE);
        angle = createAngleProperty();
        flipPane = new StackPane();

        flipPane.setPadding(new Insets(0));
        flipPane.getChildren().setAll(createBackNode(frontCase,b), createFrontNode(backCase,b));
        flipPane.widthProperty().addListener(changed -> recalculateTransformation(angle.doubleValue()));
        flipPane.heightProperty().addListener(changed -> recalculateTransformation(angle.doubleValue()));
    }

    /**
     * Create the front side of the button
     * @param functionFront
     * @return
     */
    private StackPane createFrontNode(Runnable functionFront,boolean b) {
        final StackPane node = new StackPane();
        node.setEffect(transform);
        node.visibleProperty().bind(flippedProperty);
        node.getChildren().setAll(createButton("fb", functionFront,b));
        return node;
    }

    /**
     * Create the back side of the button
     * @param functionBack
     * @return
     */
    private StackPane createBackNode(Runnable functionBack,boolean b) {
        final StackPane node = new StackPane();
        node.setEffect(transform);
        node.visibleProperty().bind(flippedProperty.not());
        node.getChildren().setAll(createButton("", functionBack,b));
        return node;
    }


    /**
     * Fuse the two sides in order to create a button with a runnable action
     * @param text
     * @param action
     * @return
     */
    private Button createButton(final String text, Runnable action,boolean b) {

        final Button button;
        iv1.setFitHeight(71);
        iv1.setFitWidth(550);
        iv2.setFitHeight(71);
        iv2.setFitWidth(550);
        if (text.equals("fb")) {
            button = new Button("", iv1);
        } else {
            button = new Button("", iv2);
        }
        button.setOnAction(event -> {
            if(b){
                Game.audioManager.sfx.click.play();
            }flip();action.run();});
        return button;
    }

    /**
     * Utility method for the animation
     * @return
     */
    private SimpleDoubleProperty createAngleProperty() {
        final SimpleDoubleProperty a = new SimpleDoubleProperty(HALF_PI);
        a.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(final ObservableValue<? extends Number> obsValue, final Number oldValue, final Number newValue) {
                recalculateTransformation(newValue.doubleValue());
            }
        });
        return a;
    }

    /**
     * Initialise the animation with the duration
     * @return
     */
    private Timeline createAnimation() {
        return new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(angle, HALF_PI)),
                new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), new KeyValue(angle, 0, Interpolator.EASE_IN)),
                new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent arg0) {
                        flippedProperty.set(flippedProperty.not().get());
                    }
                }),
                new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), new KeyValue(angle, PIE)),
                new KeyFrame(Duration.millis(ANIMATION_DURATION), new KeyValue(angle, HALF_PI, Interpolator.EASE_OUT))
        );
    }

    /**
     * Flip action on the button that pass from Front and Back or Back to Front
     */
    private void flip() {
        if (animation == null)
            animation = createAnimation();
        animation.setRate(flippedProperty.get() ? ANIMATION_RATE : -ANIMATION_RATE);
        animation.play();
    }

    /**
     * This method recalculate the angle in order to do the animation
     * @param angle
     */
    private void recalculateTransformation(final double angle) {
        final double insetsTop = flipPane.getInsets().getTop() * 2;
        final double insetsLeft = flipPane.getInsets().getLeft() * 2;
        final double radius = flipPane.widthProperty().subtract(insetsLeft).divide(2).doubleValue();
        final double height = flipPane.heightProperty().subtract(insetsTop).doubleValue();
        final double back = height / 10;
        //You may Google "Affine Transformation - Rotation"
        transform.setUlx(radius - Math.sin(angle) * radius);
        transform.setUly(0 - Math.cos(angle) * back);
        transform.setUrx(radius + Math.sin(angle) * radius);
        transform.setUry(0 + Math.cos(angle) * back);
        transform.setLrx(radius + Math.sin(angle) * radius);
        transform.setLry(height - Math.cos(angle) * back);
        transform.setLlx(radius - Math.sin(angle) * radius);
        transform.setLly(height + Math.cos(angle) * back);
    }




    //Audio Utility method
    private static void stateMusic() {
        if (Game.switchCaseMusic) {
            Game.audioManager.updateVolumeMusic(100);
            Game.switchCaseMusic = !Game.switchCaseMusic;
        } else {
            Game.audioManager.updateVolumeMusic(0);
            Game.switchCaseMusic = !Game.switchCaseMusic;
        }
    }

    private static void stateSoundFx() {
        if (Game.switchCaseSoundEffects) {
            Game.audioManager.updateVolumeFx(100);
            Game.switchCaseSoundEffects = !Game.switchCaseSoundEffects;
        } else {
            Game.audioManager.updateVolumeFx(100);
            Game.switchCaseSoundEffects = !Game.switchCaseSoundEffects;
        }
    }
}




