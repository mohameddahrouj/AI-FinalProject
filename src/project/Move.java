package project;

public class Move {
	private char block;		// ID representing the block to move
	private int moves;		// How many times the block moved so far?
	private Board board;	// Board represetnting current move 

	public Move(char block, int moves, Board board) {
		this.block = block;
		this.moves = moves;
		this.board = board;
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
	
	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
}
