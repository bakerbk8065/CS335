package cs335.convexHullFinder;

import java.awt.geom.Point2D;
import java.util.List;

public interface ConvexHullFinder {
    // compute hull and return as list of ccw points
    public List<Point2D> computeHull(List<Point2D> points);
}
