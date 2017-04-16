package project;
import java.util.ArrayList;

/**
 * State representing the parking lot
 * State representation: char 2D array
 * @author Mohamed Dahrouj
 */
public class ParkingLot {

    char[][] vehicles;
    private int N; 			//Length of matrix
    private int priority = -1;
    char blockMoved = '?'; 	//Last block moved
    int spacesMoved = 0; 	//The number of tiles the block was moved
    
    public ParkingLot(char[][] vehicles) {
        // Generates a board from a NxN array of vehicles
        N = vehicles.length;
        this.vehicles = new char[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
    		    this.vehicles[i][j] = vehicles[i][j];
    }

    public ParkingLot(char[][] vehicles, char block, int moves) {
    	// Generates a board from a NxN array of vehicles
        this(vehicles);
        this.blockMoved = block;
        this.spacesMoved = moves;
    }
    
    public int dimension() {
        return N;
    }
    
    
    public int priority() {
        if (priority != -1){
        	//If priority has not been changed get it, otherwise return cached value
        	return priority;
        }
        if (isGoal()){
        	return 0;
        }
        int count = 0;
        char value;
        //Row were main block and goal are
        int i = N%2 == 0? N/2 - 1: N/2;
        
        for (int j = 0; j < N; j++) {
        	//Value from current cell in blocks
        	value = vehicles[i][j];
            
            if (value == '-') continue;		
            if (value == 'X') {
            	j++;
            	continue;
            }
            count++;
        }
    	
        //Store the value and return it
        priority = count;        
        return priority;
    }
    
    public boolean isGoal() {
    	//Row were main block and goal are
    	int i = N%2 == 0? N/2 - 1 : N/2;
    	
    	//Checks to see if block is in the goal position
    	return vehicles[i][N-1] == 'X';
    }
    
    
    public boolean equals(Object o) {
    	//Validates board
        if (!(o instanceof ParkingLot)) return false;
        ParkingLot b = (ParkingLot) o;
        if (dimension() != b.dimension()) return false;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (vehicles[i][j] != b.vehicles[i][j]) return false;
        return true;
    }
    
    // Deep copy board
    private char[][] cloneBoard() {
        char[][] copy = new char[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                copy[i][j] = vehicles[i][j];

        return copy;
    }
    
    //Generates the successors for the production system
    public ArrayList<ParkingLot> productionSystem() {
        // all children boards
        ArrayList<ParkingLot> neighbors = new ArrayList<>();

        int tempi = 0, tempj = 0;
        char[][] tempBoard = cloneBoard();
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tempBoard[i][j] == '-') {
                	
                	//Move down
                	if (i > 0 && tempBoard[i-1][j] != '-') {
                	    char value = tempBoard[i-1][j];
                	    if (i > 1 && value == tempBoard[i-2][j]) {
                		    tempi = i - 1;
                		    while (tempi < N - 1 && tempBoard[tempi+1][j] == '-') {
		                        int aux = tempi - 1;
		                	    while (aux > 0 && tempBoard[aux-1][j] == value)
		                		    aux--;
		                	    swapAndStore(tempBoard, aux, ++tempi, j, j);
		                	    ParkingLot b = new ParkingLot(tempBoard, value, tempi - i + 1);
		                        neighbors.add(b);
                		    }
                	    }   
		                
                	    tempBoard = cloneBoard();
                    }
                	
                	//Move right
                	if (j > 0 && tempBoard[i][j-1] != '-') {
                	    char value = tempBoard[i][j-1];
                	    if (j > 1 && value == tempBoard[i][j-2]) {
                		    tempj = j - 1;
                		    while (tempj < N - 1 && tempBoard[i][tempj+1] == '-') {
		                        int aux = tempj - 1;
		                        while (aux > 0 && tempBoard[i][aux-1] == value)
		                		    aux--;
		                	    swapAndStore(tempBoard, i, i, aux, ++tempj);
		                	    ParkingLot b = new ParkingLot(tempBoard, value, tempj - j + 1);
		                        neighbors.add(b);
                		    }
                	    }   
		                
                	    tempBoard = cloneBoard();
                    }
                	                	
                	//Move Up
                	if (i < N - 1 && tempBoard[i+1][j] != '-') {
                		char value = tempBoard[i+1][j];
                	    if (i < N - 2 && value == tempBoard[i+2][j]) {
                		    tempi = i + 1;
                		    while (tempi > 0 && tempBoard[tempi-1][j] == '-') {
		                        int aux = tempi + 1;
		                	    while (aux < N - 1 && tempBoard[aux+1][j] == value)
		                		    aux++;
		                	    swapAndStore(tempBoard, aux, --tempi, j, j);
		                	    ParkingLot b = new ParkingLot(tempBoard, value, tempi - i - 1);
		                        neighbors.add(b);
                		    }
                	    }   
		                tempBoard = cloneBoard();
                    }
                	
                	//Move Left
                	if (j < N - 1 && tempBoard[i][j+1] != '-') {
                		char value = tempBoard[i][j+1];
                	    if (j < N - 2 && value == tempBoard[i][j+2]) {
                		    tempj = j + 1;
                		    while (tempj > 0 && tempBoard[i][tempj-1] == '-') {
		                        int aux = tempj + 1;
		                	    while (aux < N - 1 && tempBoard[i][aux+1] == value)
		                		    aux++;
		                	    swapAndStore(tempBoard, i, i, aux, --tempj);
		                	    ParkingLot b = new ParkingLot(tempBoard, value, tempj -j - 1);
		                        neighbors.add(b);
                		    }
                	    }   
		                tempBoard = cloneBoard();
                    }               
                }
            }
        }
        
        return neighbors;
    }
    
	//Swaps and stores two values in an array
    private void swapAndStore(char[][] array, int row1, int row2, int col1, int col2) {
        char temp = array[row1][col1];
        array[row1][col1] = array[row2][col2];
        array[row2][col2] = temp;
    }
    
    //String representation of the board
    public String toString() {
        String s = "Vehicle Moved: " + blockMoved + ", " + "Spaces Moved: " +spacesMoved + "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                s += vehicles[i][j] + " ";

            s += "\n";
        }

        return s;
    }
    
    public Move getMove() {
    	return new Move(blockMoved, spacesMoved, this);
    }
    
    public double getHeuristic(int algorithmNumber) {
    	int heuristic = 0;
    	if (algorithmNumber == 1){
    		heuristic = evaluateAStarHeuristic();
    	}
    	if (algorithmNumber == 3){
    		heuristic = evaluateAnnealingHeuristic();
    	}
    	return heuristic;
    }
    
	//Modified Euclidean Distance
	private int evaluateAStarHeuristic() {
		return (int) (priority * 
				Math.sqrt(
					Math.abs(Math.pow(getBlockedCarColumn() - getBlockedCarRow(), 2))
					+ 
					Math.abs(Math.pow(getGoalColumn() - getGoalRow(), 2))
				));
		
	}
    
	//Modified Manhattan Heuristic
    private int evaluateAnnealingHeuristic() {
    	return priority * 
    			(Math.abs(getBlockedCarColumn() 
    			- getBlockedCarRow())
    			+ Math.abs((N%2 == 0? N/2 - 1 : N/2) - (N-1)));
	}

	private int getBlockedCarColumn() {
		for (int i = 0; i < N; i++){
			for (int j = 0; j < N; j++){
				if(vehicles[i][j] == 'X'){
            		return j;
            	}
			}
		}
		return 0;
	}
    
	private int getBlockedCarRow() {
		return N%2 == 0? N/2 - 1: N/2;
	}
	
	private int getGoalRow(){
		return N%2 == 0? N/2 - 1 : N/2;	
	}
	
	private int getGoalColumn(){
		return N-1;	
	}
}
