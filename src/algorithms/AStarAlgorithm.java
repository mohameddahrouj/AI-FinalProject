package algorithms;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import datastructures.MinPriorityQueue;
import datastructures.Stack;
import project.Move;
import project.Board;
import project.Algorithm;

public class AStarAlgorithm extends Algorithm{
	private class SearchNode implements Comparable<SearchNode> {
    	private Board board;
    	private int moves;
    	private SearchNode previous;
    	private int priority;
    	
    	public SearchNode(Board b, int moves, SearchNode previous) {
    		this.board = b;
    		this.moves = moves;
    		this.previous = previous;
    		this.priority = b.priority() + this.moves;
    	}
    	
		public int compareTo(SearchNode sn) {
			if (priority < sn.priority) return -1;
    		else if (priority == sn.priority) return 0;
    		else return 1;
		}
    }
	
	/**
	 *  Find the solution of the initial board using the A* algorithm.	 *
	 * @param initial The Board to be solved
	 */
	public AStarAlgorithm(Board initial) {
    	MinPriorityQueue<SearchNode> pq = new MinPriorityQueue<SearchNode>();
    	ArrayList<Board> explored = new ArrayList<Board>();
    	
    	time = System.currentTimeMillis();
    	
    	pq.insert(new SearchNode(initial, 0, null));    	
    	SearchNode sn = null;
    	
    	while (!pq.isEmpty()) {
    		sn = pq.delMin();  
    		if (explored.contains(sn.board)) continue;
    		if (sn.board.isGoal()) break;
			explored.add(sn.board);
			expNodes++;
    		for (Board b: sn.board.neighbors()) {
    			if (!explored.contains(b)) {
    				pq.insert(new SearchNode(b, sn.moves + 1, sn));
    			}
    		}
    	} 
    	
    	time = System.currentTimeMillis() - time;
    	
    	if (pq.isEmpty()) {
    		solvable = false;
    		return;
    	}
    	
    	SearchNode prev = sn;
    	movements = new Stack<Move>();
    		
    	while (prev != null) {
    		movements.push(prev.board.getAction());
    		moves++;
    		prev = prev.previous;
    	}
    	solvable = true;
    }

	public long getRunningTime() {
		return time;
	}

	public int expandedNodes() {
		return expNodes;
	}

	public boolean isSolvable() {
		return solvable;
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
        

        Board initial = new Board(blocks);
        
        // solve the puzzle
        AStarAlgorithm solver = new AStarAlgorithm(initial);

        // print solution to standard output       
        System.out.println("Minimum number of moves = " + solver.moves());
        
        for (Move a : solver.solution())
        	System.out.println(a);
        
        System.out.println(solver.expNodes);
	}
}