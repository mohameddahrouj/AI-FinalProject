package project;
import datastructures.Stack;

/**
 * Abstract class for search algorithms
 * Each class has its own Node class as opposed to sharing one
 * @author Mohamed Dahrouj
 *
 */
public abstract class Algorithm {
	protected boolean solvable;
	protected int moves = -1;
	protected Stack<Move> movements;
	protected int expNodes = 0;
	protected long time = 0;
		
	/**
	 *  Is board solvable?
	 *  @return true if the Board is solvable.
	 */
	protected abstract boolean isSolvable();

	/**
	 *  Number of moves in solution
	 *  @return The number of moves in the solution
	 */
	protected abstract int moves();
	
	/**
	 *  Get the number of expanded nodes
	 *  @return The number of expanded nodes
	 */
	protected abstract int expandedNodes();
	
	/**
	 *  Get the time taken by the algorithm to solve the parking lot
	 *  @return The running time of the algorithm
	 */	
    protected abstract long getRunningTime();

	/**
	 *  Gets the solution to the solver
	 *  @return Solution to the specified parking lot configuration
	 */
    protected abstract Iterable<Move> solution();
}
