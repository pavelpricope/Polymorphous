package com.paradroid.util.physics;

import com.paradroid.main.Game;
import com.paradroid.main.Handlers.Handler;
import com.paradroid.main.Objects.GameObject;
import com.paradroid.main.Objects.ID;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

/**
 * @author pxp660
 */

public class Collision {

    private static Handler handler = Game.getHandler();

    public Collision() {

    }

    public static int checkCollision(Polygon polygon, ID id) {
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (tempObject.getId() == id) {


                Point2D p1 = new Point2D(tempObject.getBounds().getPoints().get(0), tempObject.getBounds().getPoints().get(1));
                Point2D p2 = new Point2D(tempObject.getBounds().getPoints().get(2), tempObject.getBounds().getPoints().get(3));
                Point2D p3 = new Point2D(tempObject.getBounds().getPoints().get(4), tempObject.getBounds().getPoints().get(5));
                Point2D p4 = new Point2D(tempObject.getBounds().getPoints().get(6), tempObject.getBounds().getPoints().get(7));


                Point2D l_p1 = new Point2D(polygon.getPoints().get(0), polygon.getPoints().get(1));
                Point2D l_p2 = new Point2D(polygon.getPoints().get(2), polygon.getPoints().get(3));
                Point2D l_p3 = new Point2D(polygon.getPoints().get(4), polygon.getPoints().get(5));
                Point2D l_p4 = new Point2D(polygon.getPoints().get(6), polygon.getPoints().get(7));

                if (GeometryUtil.checkIntersection(l_p1, l_p2, p2, p3) || GeometryUtil.checkIntersection(l_p3, l_p4, p2, p3))     // 4 3 || 2 3
                    return 1;
                else if (GeometryUtil.checkIntersection(l_p4, l_p1, p3, p4) || GeometryUtil.checkIntersection(l_p2, l_p3, p4, p3))     // 1 4 || 3 4
                    return 2;
                else if (GeometryUtil.checkIntersection(l_p1, l_p2, p4, p1) || GeometryUtil.checkIntersection(l_p3, l_p4, p4, p1))     // 2 1 || 4 1
                    return 3;
                else if (GeometryUtil.checkIntersection(l_p4, l_p1, p1, p2) || GeometryUtil.checkIntersection(l_p2, l_p3, p1, p2))     // 1 2 || 3 2
                    return 4;


            }

        }
        return 0;


    }

    public static boolean checkPolygonsCollision(Polygon polygon1, Polygon polygon2) {


        Point2D p1 = new Point2D(polygon2.getPoints().get(0), polygon2.getPoints().get(1));
        Point2D p2 = new Point2D(polygon2.getPoints().get(2), polygon2.getPoints().get(3));
        Point2D p3 = new Point2D(polygon2.getPoints().get(4), polygon2.getPoints().get(5));
        Point2D p4 = new Point2D(polygon2.getPoints().get(6), polygon2.getPoints().get(7));


        Point2D l_p1 = new Point2D(polygon1.getPoints().get(0), polygon1.getPoints().get(1));
        Point2D l_p2 = new Point2D(polygon1.getPoints().get(2), polygon1.getPoints().get(3));
        Point2D l_p3 = new Point2D(polygon1.getPoints().get(4), polygon1.getPoints().get(5));
        Point2D l_p4 = new Point2D(polygon1.getPoints().get(6), polygon1.getPoints().get(7));


        if (GeometryUtil.checkIntersection(l_p4, l_p1, p3, p4))     // 1 4
            return true;
        if (GeometryUtil.checkIntersection(l_p4, l_p1, p1, p2))     // 1 2
            return true;
        if (GeometryUtil.checkIntersection(l_p4, l_p1, p2, p3))     // 1 3
            return true;
        if (GeometryUtil.checkIntersection(l_p1, l_p2, p3, p4))     // 2 4
            return true;
        if (GeometryUtil.checkIntersection(l_p1, l_p2, p4, p1))     // 2 1
            return true;
        if (GeometryUtil.checkIntersection(l_p1, l_p2, p2, p3))     // 2 3
            return true;
        if (GeometryUtil.checkIntersection(l_p2, l_p3, p4, p1))     // 3 1
            return true;
        if (GeometryUtil.checkIntersection(l_p2, l_p3, p1, p2))     // 3 2
            return true;
        if (GeometryUtil.checkIntersection(l_p2, l_p3, p3, p4))     // 3 4
            return true;
        if (GeometryUtil.checkIntersection(l_p3, l_p4, p4, p1))     // 4 1
            return true;
        if (GeometryUtil.checkIntersection(l_p3, l_p4, p1, p2))     // 4 2
            return true;
        if (GeometryUtil.checkIntersection(l_p3, l_p4, p2, p3))     // 4 3
            return true;


        return false;


    }
}
