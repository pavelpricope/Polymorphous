package com.polymorphous.util.menuHandler;

import com.polymorphous.main.Game;
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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;

public class FlipCanvas {
    /**
     * @author jxv603
     */

    private static final Double PIE = Math.PI;
    private static final Double HALF_PIE = Math.PI / 2;
    private static final double ANIMATION_DURATION = 5000;
    private static final double ANIMATION_RATE = 10;
    private SimpleDoubleProperty angle = new SimpleDoubleProperty(HALF_PIE);
    private PerspectiveTransform transform = new PerspectiveTransform();
    private SimpleBooleanProperty flippedProperty = new SimpleBooleanProperty(true);
    private Timeline animation;
    private StackPane flipPane;


    public FlipCanvas() {

    }

    /**
     * Create the animation Menu
     * @param menuPage
     * @return
     */
    public StackPane createAnimationMenu(MenuPages menuPage) {
        angle = createAngleProperty();
        flipPane = new StackPane();
        flipPane.setPadding(new Insets(0));
        flipPane.getChildren().setAll(createNode(menuPage));
        flipPane.widthProperty().addListener(changed -> recalculateTransformation(angle.doubleValue()));
        flipPane.heightProperty().addListener(changed -> recalculateTransformation(angle.doubleValue()));
        return flipPane;
    }

    /**
     * Create the two sides of the flippable canvas
     * @param menuPage
     * @return
     */
    private VBox createNode(MenuPages menuPage) {
        final VBox pane = new VBox(15);
        pane.setEffect(transform);
        pane.visibleProperty().bind(menuPage.animation ? flippedProperty : flippedProperty.not());
        for (int i = 0; i < menuPage.nodes.length; i++) {
            pane.getChildren().add(menuPage.nodes[i]);
            final Runnable r = menuPage.actions[i];
            final MenuPages page = menuPage.menus[i];
            if (menuPage.nodes[i] instanceof Button)
                ((Button) menuPage.nodes[i]).setOnAction(event -> {
                    if (r != null)
                        r.run();
                    if (page != null) {
                        flipPane.getChildren().setAll(createNode(menuPage), createNode(page));
                        Game.audioManager.sfx.click.play();
                        flip();
                    }
                });
        }
        return pane;
    }


    private SimpleDoubleProperty createAngleProperty() {
        final SimpleDoubleProperty angle = new SimpleDoubleProperty(HALF_PIE);
        angle.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(final ObservableValue<? extends Number> obsValue, final Number oldValue, final Number newValue) {
                recalculateTransformation(newValue.doubleValue());
            }
        });
        return angle;
    }

    private Timeline createAnimation() {
        return new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(angle, HALF_PIE)),
                new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), new KeyValue(angle, 0, Interpolator.EASE_IN)),
                new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent arg0) {
                        flippedProperty.set(flippedProperty.not().get());
                    }
                }),
                new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), new KeyValue(angle, PIE)),
                new KeyFrame(Duration.millis(ANIMATION_DURATION), new KeyValue(angle, HALF_PIE, Interpolator.EASE_OUT))
        );
    }

    //Flip Action Method And Math Stuff
    public void flip() {
        if (animation == null)
            animation = createAnimation();
        animation.setRate(flippedProperty.get() ? ANIMATION_RATE : -ANIMATION_RATE);
        animation.play();
    }

    private void recalculateTransformation(final double angle) {
        final double insetsTop = flipPane.getInsets().getTop() * 2;
        final double insetsLeft = flipPane.getInsets().getLeft() * 2;
        final double radius = flipPane.widthProperty().subtract(insetsLeft).divide(2).doubleValue();
        final double height = flipPane.heightProperty().subtract(insetsTop).doubleValue();
        final double back = height / 10;
        /*
         * You may Google "Affine Transformation - Rotation"
         */
        transform.setUlx(radius - Math.sin(angle) * radius);
        transform.setUly(0 - Math.cos(angle) * back);
        transform.setUrx(radius + Math.sin(angle) * radius);
        transform.setUry(0 + Math.cos(angle) * back);
        transform.setLrx(radius + Math.sin(angle) * radius);
        transform.setLry(height - Math.cos(angle) * back);
        transform.setLlx(radius - Math.sin(angle) * radius);
        transform.setLly(height + Math.cos(angle) * back);
    }
}




