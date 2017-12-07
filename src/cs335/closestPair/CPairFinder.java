package cs335.closestPair;

import java.util.Collections;
import java.util.List;

//example code written and supplied by Dan Stevenson

public class CPairFinder {
        
    public CPairFinder() {
    }
    
    public int findMin(List l) {
        
        // Sort l into x order
        Collections.sort(l);   // nLog(n)
        
        System.out.println(l);
        
        return recFindMin(l);
    }
    
    // Assumes list is sorted
    private int recFindMin(List<Point1D> l) {
        System.out.println(l);
        
//      if (l.size() == 1) {
//          
//          return Integer.MAX_VALUE;  // comment the heck out of this
//          
//      } else {
        
        if (l.size() == 2) {  // Base case
            
            return l.get(0).dist(l.get(1));
            
        } else if (l.size() == 3) {  // Base case
            
            return Math.min(l.get(0).dist(l.get(1)), l.get(1).dist(l.get(2)));
            
        } else{
            // Rec case
            //   Break into 2
            List<Point1D> left = l.subList(0, l.size()/2);
            List<Point1D> right = l.subList(l.size()/2, l.size());
            
            //   Solve each piece
            int leftMin = recFindMin(left);
            int rightMin = recFindMin(right);
            
            //   Combo results
            int minMin = left.get(left.size()-1).dist(right.get(0));
            int min = Math.min(Math.min(leftMin, rightMin), minMin);
            
            return min;
        }
    
    }

}
