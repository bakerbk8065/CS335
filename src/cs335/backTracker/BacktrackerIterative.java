package cs335.backTracker;

import java.util.LinkedList;
import java.util.List;

public class BacktrackerIterative {

	private int numberExpanded = 0;

	public State backtrack(State s) {

		State soln = null;

		int bestSolnCost = Integer.MAX_VALUE;

		List<State> statesToProcess = new LinkedList<State>();
		statesToProcess.add(0, s);  // push

		while (!statesToProcess.isEmpty()) {

			System.out.println(statesToProcess);

			// Pop a feasible state from the list
			State currentState = statesToProcess.remove(0);
			numberExpanded++;

			if (currentState.isSolved()) {

				System.out.println("Solved: " + currentState);

				if (currentState.getBound() < bestSolnCost) {
					bestSolnCost = currentState.getBound();
					soln = currentState;
				}
				//result.add(s);

			} else {

				while((currentState.hasMoreChildren()) &&
				      (currentState.getBound() < bestSolnCost)) { //&& result.isEmpty()) {

					State child = currentState.nextChild();

					if (child.isFeasible()) {
						statesToProcess.add(child);
					}
				}
			}
		}
		return soln;
	}
}
