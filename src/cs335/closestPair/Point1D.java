package cs335.closestPair;

//example code written and supplied by Dan Stevenson

public class Point1D implements Comparable<Point1D> {
    private int x;
    
    public Point1D(int x) {
        this.x = x;
    }
    
    public int dist(Point1D p) {
        return Math.abs(p.x - this.x);
    }
    
    public String toString() {
        return x + "";
    }

    @Override
    public int compareTo(Point1D p) {
        return (this.x - p.x);
    }
}
