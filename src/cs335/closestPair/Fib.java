package cs335.closestPair;

//example code written and supplied by Dan Stevenson

public class Fib {
    
    public static int fib(int n) {
        
        if (n == 0) {
            return 1;
        } else if (n == 1) {
            return 1;
            
        } else { // rec 
            
            return fib(n-1) + fib(n-2);         
        }
        
    }

    public static void main(String[] args) {
        System.out.println(fib(100));

    }

}