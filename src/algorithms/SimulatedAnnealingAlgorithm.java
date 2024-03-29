package algorithms;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import datastructures.MinPriorityQueue;
import datastructures.Stack;
import project.Move;
import project.ParkingLot;
import project.Algorithm;

public class SimulatedAnnealingAlgorithm extends Algorithm{
	private class SearchNode implements Comparable<SearchNode> {
    	private ParkingLot parkingLot;
    	private int moves;
    	private SearchNode previous;
    	private int priority;
    	
    	public SearchNode(ParkingLot b, int moves, SearchNode previous) {
    		this.parkingLot = b;
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
	
	public static double maxEvaluatedEver = 0;
	public static final String METRIC_NODES_EXPANDED = "nodesExpanded";
	public static final String METRIC_TEMPERATURE = "temp";
	public static final String METRIC_NODE_VALUE = "nodeValue";
	
	public static ParkingLot getRandomNeighbour(ArrayList<ParkingLot> list) {
		Random r = new Random();
		return list.get(r.nextInt(list.size()));
	}
	
	// if E > 0 then current <- next
	// else current <- next only with probability e^(E/T)
	private boolean shouldAccept(double temperature, double deltaE) {
		return (deltaE > 0.0) 
				|| (new Random().nextDouble() <= probabilityOfAcceptance(
						temperature, deltaE));
	}

	// assumption greater heuristic value => HIGHER on hill;
	// 0 == goal state;
	// Simulated annealing deals with gradient descent
	private double getValue(SearchNode n) {
		return -1 * n.parkingLot.getHeuristic(3);
	}
	
	/**
	 * Returns e^(deltaE/T)
	 * @param temperature controlling the probability of downward steps
	 * @param deltaE value of next minus current
	 */
	public double probabilityOfAcceptance(double temperature, double deltaE) {
		return Math.exp(deltaE / temperature);
	}
			
	/**
	 * Find the solution of the initial board using the simulated annealing algorithm.
	 * @param initial The Board to be solved
	 */
	public SimulatedAnnealingAlgorithm(ParkingLot initial, Scheduler scheduler) {
		time = System.currentTimeMillis();
		
		solvable = false;
		// current <- MAKE-NODE(problem.INITIAL-STATE)
		SearchNode current = null;
		SearchNode next = null;
		// for t = 1 to INFINITY do
		int timeStep = 0;
		
		ArrayList<ParkingLot> visitedBoards = new ArrayList<>();
		MinPriorityQueue<SearchNode> pq = new MinPriorityQueue<SearchNode>();
		pq.insert(new SearchNode(initial, 0, null));
		
		while (!pq.isEmpty()) {
			current = pq.delMin(); 
    		if (visitedBoards.contains(current.parkingLot)) continue;
    		if (current.parkingLot.isGoal()) break;
    		visitedBoards.add(current.parkingLot);
			// temperature <- schedule(t)
			double temperature = scheduler.getTemp(timeStep);
			timeStep++;
			// if temperature = 0 then return current
			if (temperature == 0.0) {
				if (current.parkingLot.isGoal()){
					solvable = true;
					break;
				}
				else
					return;
			}
			
			expNodes++;
				for(ParkingLot b: current.parkingLot.productionSystem()){
					// next <- a randomly selected successor of current
					next = new SearchNode(b, current.moves+1, current);
					// /\E <- next.VALUE - current.value
					double deltaE = getValue(next) - getValue(current);
					if (!visitedBoards.contains(b)) {
						if(shouldAccept(temperature, deltaE)){
							pq.insert(new SearchNode(b, current.moves+1, current));
						}
					}
				}
		}
		
		time = System.currentTimeMillis() - time;
    	
    	if (pq.isEmpty()) {
    		solvable = false;
    		return;
    	}
		
    	SearchNode prev = current;
    	movements = new Stack<Move>();
    		
    	while (prev != null) {
    		movements.push(prev.parkingLot.getMove());
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
		File file = new File("parkingLots/Easy-02.parking");
		
		Scanner in = null;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {}
		
		in.next();
		
        int N = 6;
        char[][] blocks = new char[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.next().charAt(0);
        ParkingLot initial = new ParkingLot(blocks);
        Scheduler s = new Scheduler(20, 0.0000045, 100000000);
        SimulatedAnnealingAlgorithm solver = new SimulatedAnnealingAlgorithm(initial, s);      
        System.out.println("Minimum number of moves = " + solver.moves());
        for (Move a: solver.solution())
        	System.out.println(a);
        
        System.out.println(solver.expNodes);
	}
}
