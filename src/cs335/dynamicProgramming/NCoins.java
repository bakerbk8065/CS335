package cs335.dynamicProgramming;

import java.util.ArrayList;
import java.util.List;

public class NCoins {
	
	private List<Integer> minCoins;
	private List<Integer> coinUsed;
	
	public NCoins(List<Integer> coins) {
		
		minCoins = new ArrayList<Integer>();
		coinUsed = new ArrayList<Integer>();
		
		// Fill in base case
		minCoins.add(0);
		coinUsed.add(0);
		
		// Recursive case
		
		// Fill in each slot from 0 to 99
		for (int i=1; i<100; i++) {
		
			int minSoFar = Integer.MAX_VALUE;
			int minCoinSoFar = -1;
			
			// Check each coin
			for (int j=0; j<coins.size(); j++) {
				int currentCoin = coins.get(j);
				
				if (i - currentCoin >= 0) {
					int currentOption = minCoins.get(i - currentCoin) + 1;
					if (currentOption < minSoFar) {
						minSoFar = currentOption;
						minCoinSoFar = currentCoin;
					}
				}
			}
			
			minCoins.add(minSoFar);
			coinUsed.add(minCoinSoFar);
		}
		
		
		System.out.println(coinUsed);
	}
	
	public int findMin(int n) {
		
		return minCoins.get(n);
	}
	
	public List<Integer> findChange(int n) {
		
		List<Integer> result;
		
		
//		int currentN = n;
//		while (currentN > 0) {
//			result.add(coinUsed.get(currentN));
//			currentN -= coinUsed.get(currentN);
//		}
		
		if (n == 0) {
			result = new ArrayList<Integer>();
		} else {
			result = findChange(n - coinUsed.get(n));
			result.add(coinUsed.get(n));
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		List<Integer> coins = new ArrayList<Integer>();
		
		coins.add(25);
		coins.add(12);
		coins.add(10);
		coins.add(5);
		coins.add(1);
		
		NCoins nc = new NCoins(coins);
		
		System.out.println(nc.findMin(29));
		System.out.println(nc.findChange(29));
		
		
		
		
	}

}
