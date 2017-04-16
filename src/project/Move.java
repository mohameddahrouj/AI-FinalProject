package project;

public class Move {
	private char block;		// ID representing the block to move
	private int moves;		// How many times the block moved so far?
	private ParkingLot parkingLot;	// Board representing current move 

	public Move(char block, int moves, ParkingLot parkingLot) {
		this.block = block;
		this.moves = moves;
		this.parkingLot = parkingLot;
	}
	
	public char getBlock() {
		return block;
	}
	
	public int getMoves() {
		return moves;
	}
	
	/**
	 *  Get the block expressed as a String
	 */
	public String toString() {
		return "{" + block + ", " + moves + "}";
	}
	
	public ParkingLot getBoard() {
		return parkingLot;
	}

	public void setBoard(ParkingLot parkingLot) {
		this.parkingLot = parkingLot;
	}
}
