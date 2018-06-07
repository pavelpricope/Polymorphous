package com.polymorphous.util.physics;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

/**
 * @author pxp660
 */

public class GeometryUtil {

    public static boolean areColinear(Point2D a, Point2D b, Point2D c) {

        double twiceSignedArea = twiceSignedArea(a, b, c);

        return twiceSignedArea == 0;
    }

    public static double twiceSignedArea(Point2D a, Point2D b, Point2D c) {

        double twiceSignedArea = (b.getX() - a.getX()) * (c.getY() - a.getY())
                - (b.getY() - a.getY()) * (c.getX() - a.getX());

        return twiceSignedArea;
    }

    public static boolean isAnticlockWiseTurn(Point2D a, Point2D b, Point2D c) {

        double twiceSignedArea = twiceSignedArea(a, b, c);

        return twiceSignedArea > 0;
    }

    public static boolean checkIntersection(Point2D a, Point2D b, Point2D c, Point2D d) {

        if (isAnticlockWiseTurn(a, c, d) == isAnticlockWiseTurn(b, c, d)) {
            return false;
        } else if (isAnticlockWiseTurn(a, b, c) == isAnticlockWiseTurn(a, b, d)) {
            return false;
        } else {
            return true;
        }

    }

    // check if a point is on a segment
    public static boolean onSegment(Point2D a, Point2D b, Point2D c) {

        if (a.getX() <= Math.max(b.getX(), c.getX()) && a.getX() >= Math.min(b.getX(), c.getX())
                && a.getY() <= Math.max(b.getY(), c.getY()) && a.getY() >= Math.min(b.getY(), c.getY())) {
            return true;
        }

        return false;
    }

    public static boolean isInsidePoligon(Point2D point, Point2D[] polygon) {

        if (polygon.length < 3) {
            return false;
        }
        Point2D extreme = new Point2D(Integer.MAX_VALUE, point.getY());

        int count = 0;
        int i = 0;
        do {
            int next = (i + 1) % polygon.length;
            if (checkIntersection(polygon[i], polygon[next], point, extreme)) {

                if (areColinear(polygon[i], point, polygon[next])) {

                    return onSegment(polygon[i], point, polygon[next]);

                }
                count++;

            }
            i = next;

        } while (i != 0);

        return (count % 2 == 1);
    }

    /*TOdo  Remove unused method
    public static boolean isInsidePolygon(int x, int y, Polygon tempTile) {
        Point2D point = new Point2D(x, y);

        Point2D p1 = new Point2D(tempTile.getPoints().get(0), tempTile.getPoints().get(1));
        Point2D p2 = new Point2D(tempTile.getPoints().get(2), tempTile.getPoints().get(3));
        Point2D p3 = new Point2D(tempTile.getPoints().get(4), tempTile.getPoints().get(5));
        Point2D p4 = new Point2D(tempTile.getPoints().get(6), tempTile.getPoints().get(7));
        Point2D[] polygon = {p1, p2, p3, p4};
        if (GeometryUtil.isInsidePoligon(point, polygon)) {
            return true;
        }
        return false;
    }
    */
}
