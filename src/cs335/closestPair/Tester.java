package cs335.closestPair;

import java.util.ArrayList;
import java.util.List;

// example code written and supplied by Dan Stevenson

public class Tester {

    public static void main(String[] args) {
        
        List<Point1D> l = new ArrayList<Point1D>();
        
        l.add(new Point1D(2));
        l.add(new Point1D(8));
        l.add(new Point1D(23));
        //l.add(new Point1D(77));
        l.add(new Point1D(0));
        l.add(new Point1D(99));
        l.add(new Point1D(19));
        l.add(new Point1D(12));
        
        CPairFinder cpf = new CPairFinder();
        int result = cpf.findMin(l);
        System.out.println(result);
        
        
    }

}
