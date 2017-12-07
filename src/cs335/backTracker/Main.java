package cs335.backTracker;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {

//		BacktrackerIterative bt = new BacktrackerIterative();
//		State s = new NQueensState(4);
//		State result = bt.backtrack(s);
//		System.out.println(result);
		
		// Define the graph from the ppt
		boolean[][] graph =
			{{false, true, false, false, false, true},
			{ true, false, true, false, false, true},
			{false, true, false, true, true, false},
			{false, false, true, false, true, false},
			{false, false, true, true, false, true},
			{ true, true, false, false, true, false}};
		// Define the colors used in the ppt
		List<String> colors = new ArrayList<String>();
		colors.add("Red");
		colors.add("Green");
		colors.add("Blue");
		State s = new GraphColoringState(graph, colors);
		
		Backtracker bt = new Backtracker();
		State result = bt.backtrack(s);
		System.out.println(result);

	}

}
