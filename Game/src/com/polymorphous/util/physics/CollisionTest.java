package com.polymorphous.util.physics;

import javafx.geometry.Point2D;
import org.junit.Test;

import static com.polymorphous.util.physics.GeometryUtil.checkIntersection;
import static com.polymorphous.util.physics.GeometryUtil.isInsidePoligon;
import static org.junit.Assert.assertSame;

public class CollisionTest {

    @Test
    public void intersectionTest() {

        Point2D a = new Point2D(3, 3);
        Point2D b = new Point2D(2, 1);
        Point2D c = new Point2D(4, 1);
        Point2D d = new Point2D(4, 3);

        assertSame(checkIntersection(a, c, b, d), true);
        assertSame(checkIntersection(a, b, c, d), false);

    }

    @Test
    public void isInsidePolygonTest() {

        Point2D[] polygon = new Point2D[]{new Point2D(0, 0), new Point2D(10, 0), new Point2D(10, 10),
                new Point2D(0, 10)};
        Point2D point = new Point2D(20, 20);
        Point2D point2 = new Point2D(5, 5);

        assertSame(isInsidePoligon(point2, polygon), true);
        assertSame(isInsidePoligon(point, polygon), false);
    }


}
