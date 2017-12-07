package cs335.convexHullFinder;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class QuickHull implements ConvexHullFinder {
    
    public List<Point2D> computeHull(List<Point2D> points) {

        // make yAxis line to test leftmost and rightmost points against
        Line2D yAxis = new Line2D.Double(0,0,0,100);
        Point2D A = points.get(0);
        Point2D B = points.get(0);
        // find leftmost (A) and rightmost (B) points
        for (int i=0; i<points.size(); i++) {
            if (yAxis.ptLineDist(points.get(i)) < yAxis.ptLineDist(A)) {
                A = points.get(i);
            } else
            if (yAxis.ptLineDist(B) < yAxis.ptLineDist(points.get(i))) {
                B = points.get(i);
            }
        }
        
        // make lines to pass to recursiveQuickHull
        Line2D lineAB = new Line2D.Double(A,B);
        Line2D lineBA = new Line2D.Double(B,A);
        // make lists of points to pass to recursiveQuickHull
        List<Point2D> pointsAB = new ArrayList<Point2D>();  // above lineAB
        List<Point2D> pointsBA = new ArrayList<Point2D>();  // below lineAB
        for (int i=0; i<points.size(); i++) {
            if (lineAB.relativeCCW(points.get(i))==1) {
                pointsAB.add(points.get(i));
            } else
            if (lineAB.relativeCCW(points.get(i))==-1) {
                pointsBA.add(points.get(i));
            } 
        }
        
        // initialize hullPoints list
        List<Point2D> hullPoints = new ArrayList<Point2D>();
        // add left most point
        hullPoints.add(B);
        // add top hull
        hullPoints.addAll(recursiveQuickHull(lineAB,pointsAB));
        // add rightmost point
        hullPoints.add(A);
        // add bottom hull
        hullPoints.addAll(recursiveQuickHull(lineBA,pointsBA));

        return hullPoints;
    }
    
    
    private List<Point2D> recursiveQuickHull(Line2D lineAB, List<Point2D> pointsAB) {
        
        // initialize partialHullPoints list
        List<Point2D> partialHullPoints = new ArrayList<Point2D>();

        // base case = 1 point
        if (pointsAB.size()==1) {
            partialHullPoints.add(pointsAB.get(0));
        }
        
        if (pointsAB.size()>1) {
            // find farthest point C
            Point2D C = pointsAB.get(0);
            for (int i=0; i<pointsAB.size(); i++) {
                if (lineAB.ptLineDist(C) < lineAB.ptLineDist(pointsAB.get(i))) {
                    C = pointsAB.get(i);
                }
            }
            
            // get points A and B
            Point2D A = lineAB.getP1();
            Point2D B = lineAB.getP2();
            // make lines to pass to back to recursiveQuickHull
            Line2D lineAC= new Line2D.Double(A,C);
            Line2D lineCB = new Line2D.Double(C,B);
            
            // make lists of points to pass back to recursiveQuickHull
            List<Point2D> pointsAC = new ArrayList<Point2D>();  // above lineAC
            List<Point2D> pointsCB = new ArrayList<Point2D>();  // below lineCB
            for (int i=0; i<pointsAB.size(); i++) {
                if (lineAC.relativeCCW(pointsAB.get(i))==1) {
                    pointsAC.add(pointsAB.get(i));
                } else
                if (lineCB.relativeCCW(pointsAB.get(i))==1) {
                    pointsCB.add(pointsAB.get(i));
                } 
            }
            
            // recursive call back
            partialHullPoints.addAll(recursiveQuickHull(lineCB,pointsCB));
            partialHullPoints.add(C);
            partialHullPoints.addAll(recursiveQuickHull(lineAC,pointsAC));
        }
        
        return partialHullPoints;
    }
}
