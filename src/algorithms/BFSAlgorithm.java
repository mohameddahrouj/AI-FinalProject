package algorithms;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import datastructures.Queue;
import datastructures.Stack;
import project.Move;
import project.ParkingLot;
import project.Algorithm;

public class BFSAlgorithm extends Algorithm{
	private class SearchNode{
	    private ParkingLot parkingLot;
	    private int moves;
	   	private SearchNode previous;
	    
	    public SearchNode(ParkingLot b, int moves, SearchNode previous) {
	    	this.parkingLot = b;
	    	this.moves = moves;
	    	this.previous = previous;
	   	}
	}
			 
	/**
	 *  Find the solution of the initial board using the BFS algorithm.
	 * @param initial The Board to be solved
	 */
	public BFSAlgorithm(ParkingLot initial) {
    	Queue<SearchNode> queue = new Queue<SearchNode>();
    	ArrayList<ParkingLot> explored = new ArrayList<ParkingLot>();

    	time = System.currentTimeMillis();
    	
    	queue.enqueue(new SearchNode(initial, 0, null));    	
    	SearchNode sn = null;
    	while (!queue.isEmpty()) {
    		sn = queue.dequeue();
    		if (explored.contains(sn.parkingLot)) continue;
    		if (sn.parkingLot.isGoal()) break;
			explored.add(sn.parkingLot);

			expNodes++;
    		for (ParkingLot b: sn.parkingLot.productionSystem()) {
    			if (!explored.contains(b))
    				queue.enqueue(new SearchNode(b, sn.moves + 1, sn));
    		}
    	} 


    	if (queue.isEmpty()) {
    		solvable = false;
    		return;
    	}
    	
    	time = System.currentTimeMillis() - time;
    	
    	SearchNode prev = sn;
    	movements = new Stack<Move>();
    	
    	while (prev != null) {
    		movements.push(prev.parkingLot.getMove());
    		moves++;
    		prev = prev.previous;
    	}
    	solvable = true;
    }
	

	public boolean isSolvable() {
		return solvable;
	}

	public long getRunningTime() {
		return time;
	}

	public int expandedNodes() {
		return expNodes;
	}

	public int moves() {
		return moves;
	}

	public Iterable<Move> solution() {
		if (!solvable) return null;
    	return movements;
	}
	
	public static void main(String[] args) {
		 // create initial board from file
		File file = new File("parkingLots/Easy-02.parking");
		
		Scanner in = null;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		in.next();
		
        int N = 6;
        char[][] blocks = new char[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.next().charAt(0);
        

        ParkingLot initial = new ParkingLot(blocks);
        
        // solve the puzzle
        BFSAlgorithm solver = new BFSAlgorithm(initial);

        // print solution to standard output
        System.out.println("Minimum number of moves = " + solver.moves());
        
        for (Move a: solver.solution())
        	System.out.println(a);
	}
}
