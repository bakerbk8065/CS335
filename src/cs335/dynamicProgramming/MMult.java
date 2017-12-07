package cs335.dynamicProgramming;

public class MMult {
	
	public class MatrixThread implements Runnable {
		
		private int i;
		private int j;
		private int[][] optimalMult;
		private int[] dims;
		
		public MatrixThread(int i, int j, int[][] optimalMult, int[] dims) {
			this.i = i;
			this.j = j;
			this.optimalMult = optimalMult;
			this.dims = dims;
		}

		@Override
		public void run() {
			
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
			
			// Pick the choice that wins
			int min = Integer.MAX_VALUE;
			for (int k=i; k<j; k++) {
				
				int localMin = optimalMult[i][k] + optimalMult[k+1][j] + dims[i]*dims[k+1]*dims[j+1];
				
				if (localMin < min) {
					min = localMin;
				}
			}  // k	
			
			// update table with min
			optimalMult[i][j] = min;
			
		}
	
		
	}
	
	public MMult() {
	}
	
	public int findOptimal(int[] dims) {
		
		// Build empty table
		int n = dims.length - 1;
		int[][] optimalMult = new int[n][n];
		
		// Fill in base cases
		for (int i=0; i<n; i++) {
			optimalMult[i][i] = 0;
		}
		
		// Walk through diag by diag
		for (int diagCol=1; diagCol<n; diagCol++) {
			
			Thread[] threads = new Thread[n-diagCol];
			
			for (int diagNum=0; diagNum<(n-diagCol); diagNum++) {
				
				int i = diagNum;
				int j = diagCol + diagNum;
				
				//System.out.println(i + " " + j);
				
				MatrixThread mt = new MatrixThread(i, j, optimalMult, dims);
				threads[diagNum] = new Thread(mt);
				threads[diagNum].start();

		
			} // diagNum
			
			for (int diagNum=0; diagNum<(n-diagCol); diagNum++) {
				
				try {
					threads[diagNum].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		} // diagCol
		
		return optimalMult[0][n-1];
	}
	

	public static void main(String[] args) {
		
		MMult mm = new MMult();
		
		int[] dims = new int[7];
		dims[0] = 5;
		dims[1] = 2;
		dims[2] = 3;
		dims[3] = 4;
		dims[4] = 6;
		dims[5] = 7;
		dims[6] = 8;
		
		int result = mm.findOptimal(dims);
		System.out.println(result);
	}

}
