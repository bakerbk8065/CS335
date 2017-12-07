package cs335.convexHullFinder;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


// the final version of MergeHull that was submit for grading was lost while cleaning the file system
// this is an older version that fails sometimes for high (>200) point counts
public class MergeHull implements ConvexHullFinder {
    
    public List<Point2D> computeHull(List<Point2D> points) {
        
        // sort points from left to right
        Collections.sort(points, new Comparator<Point2D>() {
            @Override
            public int compare(Point2D a, Point2D b) {
                if (a.getX() < b.getX()) {
                    return -1;
                } else
                if (a.getX() > b.getX()) {
                    return 1;
                } else {
                    if (a.getY() < b.getY()) {
                        return -1;
                    } else
                    if (a.getY() > b.getY()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        });
        
        // call recursive function
        return recursiveMergeHull(points);
    }

    
    private List<Point2D> recursiveMergeHull(List<Point2D> points) {
        
        // initialize hullPoints list
        List<Point2D> hullPoints = new ArrayList<Point2D>();
        
        // base case = 1 or 2 points
        if (points.size()<3) {
            hullPoints=points;
        } else {
            // initialize leftPoints and rightPoints lists
            List<Point2D> leftPoints = new ArrayList<Point2D>();
            List<Point2D> rightPoints = new ArrayList<Point2D>();
            
            // split points in half and feed halves back to recursiveMergeHull
            leftPoints=recursiveMergeHull(points.subList(0,points.size()/2));
            rightPoints=recursiveMergeHull(points.subList(points.size()/2,points.size()));
            
            // find rightmost point in leftPoints (A)
            int indexA=0;
            Point2D A = leftPoints.get(0);
            for (int i=0; i<leftPoints.size(); i++) {
                if (leftPoints.get(i).getX() > A.getX()) {
                    indexA=i;
                } 
            }
            
            // find leftmost point in rightPoints (B)
            int indexB=0;
            Point2D B = rightPoints.get(0);
            for (int i=0; i<rightPoints.size(); i++) {
                if ( rightPoints.get(i).getX() < B.getX() ) {
                    indexB=i;
                } 
            }
            
            // get endpoint indices of upper tangent 
            List<Integer> results = getTangentIndices(leftPoints,rightPoints,indexA,indexB,1);
            int indexC=results.get(0);
            int indexD=results.get(1);
            
            // get endpoint indices of lower tangent 
            results = getTangentIndices(leftPoints,rightPoints,indexA,indexB,-1);
            indexA=results.get(0);
            indexB=results.get(1);
            
            // join hulls together
            for (int i=0; i<=(indexD-indexB+rightPoints.size())%rightPoints.size(); i++) {
                hullPoints.add(rightPoints.get((indexB+i)%rightPoints.size()));
            }
            for (int i=0; i<=(indexA-indexC+leftPoints.size())%leftPoints.size(); i++) {
                hullPoints.add(leftPoints.get((indexC+i)%leftPoints.size()));
            }
        }
        
        return hullPoints;
    }
    
    
    // crawl up or down two given hulls to get an upper or lower tangent
    public List<Integer> getTangentIndices(List<Point2D> leftPoints, List<Point2D> rightPoints, int indexA, int indexB, int bad) {
        List<Integer> result = new ArrayList<Integer>();
        
        Point2D A = leftPoints.get(indexA);
        Point2D B = rightPoints.get(indexB);
        Line2D lineAB = new Line2D.Double(A,B);
        
        while ((pointLine(lineAB,nextPoint(leftPoints,indexA))==bad) || 
                (pointLine(lineAB,prevPoint(leftPoints,indexA))==bad) ||
                (pointLine(lineAB,nextPoint(rightPoints,indexB))==bad) ||
                (pointLine(lineAB,prevPoint(rightPoints,indexB))==bad)) {
             
             while ((pointLine(lineAB,nextPoint(leftPoints,indexA))==bad) || 
                    (pointLine(lineAB,prevPoint(leftPoints,indexA))==bad)) {
                 A=prevPoint(leftPoints,indexA);
                 indexA=prevIndex(leftPoints,indexA);
                 lineAB.setLine(A,B);
             }
             while ((pointLine(lineAB,nextPoint(rightPoints,indexB))==bad) ||
                    (pointLine(lineAB,prevPoint(rightPoints,indexB))==bad)) {
                 B=nextPoint(rightPoints,indexB);
                 indexB=nextIndex(rightPoints,indexB);
                 lineAB.setLine(A,B);
             }
        }
        result.add(indexA);
        result.add(indexB);
        return result;
    }
    
    // return 0 if point is on line
    public int pointLine(Line2D line, Point2D point) {
        int result = 0;
        if ((line.getX1()!=point.getX() || line.getX2()!=point.getX()) || (line.ptLineDist(point)!=0)) {
            result=line.relativeCCW(point);
        }
        return result;
    }
    
    // some helper functions
    public int nextIndex(List<Point2D> points, int index) {
        return (index+1)%points.size();
    }
    public int prevIndex(List<Point2D> points, int index) {
        return (index-1+points.size())%points.size();
    }
    public Point2D nextPoint(List<Point2D> points, int index) {
        return points.get((index+1)%points.size());
    }
    public Point2D prevPoint(List<Point2D> points, int index) {
        return points.get((index-1+points.size())%points.size());
    }
}
