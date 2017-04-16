package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import algorithms.AStarAlgorithm;
import algorithms.BFSAlgorithm;
import algorithms.Scheduler;
import algorithms.SimulatedAnnealingAlgorithm;

public class ConsoleRunner{
	
	public static boolean solving = false;
	private Algorithm algorithm;
	private AlgorithmType algo;
	
	/**
	 * Creates control side bar
	 */
	public ConsoleRunner() {
		FileUtility bf = new FileUtility(new File("parkingLots/Advanced-02.parking"));
		ComboBoxListener(bf);
		algo = AlgorithmType.AStar;
		SolveButtonListener(bf, algo);
		ShowButtonListener(bf);
		printMetrics();
		
	}
	
	public void getBoardSizeIfApplicable(){
			System.out.print("Please specify the configuration (rows,cols): ");
			//Scanner scanner = new Scanner(System.in);
			//String boardSize = scanner.nextLine();
			//String[] sizeArray = boardSize.split(",");
			//scanner.close();
	}

	//Parking config action listener
	private void ComboBoxListener(FileUtility bf) {
			try {
				AppComponents.board.load(bf);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
	}

	private void SolveButtonListener(FileUtility bf, AlgorithmType algo) {
		new SolverThread(bf, algo).start();
	}
	
	//Solution action listener
	private void ShowButtonListener(FileUtility bf){
			try {
				AppComponents.board.load(bf);
				new ShowThread().start();
			} catch (FileNotFoundException e1) {}
	}
	
	private class SolverThread extends Thread {
		
		FileUtility bf;
		AlgorithmType algo;
		
		public SolverThread(FileUtility bf, AlgorithmType algo){
			this.bf = bf;
			this.algo = algo;
			
		}

		public void run() {
			
			FileUtility bFile= bf;
			Scanner in = null;
			try {
				in = new Scanner(bFile.file());
			} catch (FileNotFoundException e1) {}

			in.next();
			char[][] blocks = new char[6][6];
			for (int i = 0; i < 6; i++)
				for (int j = 0; j < 6; j++)
					blocks[i][j] = in.next().charAt(0);

			ParkingLot initial = new ParkingLot(blocks);
			
			if (algo.equals(AlgorithmType.AStar))
				algorithm = new AStarAlgorithm(initial);
			else if (algo.equals(AlgorithmType.BFS))
				algorithm = new BFSAlgorithm(initial);
			else if (algo.equals(AlgorithmType.SA))
				algorithm = new SimulatedAnnealingAlgorithm(initial, new Scheduler(20, 0.00045, 1000000));
			else {
				System.out.println("Please select a search algorithm!");
				
				solving = false;
				return;
			}
		}
	}
	
	private void printMetrics(){
		System.out.println("Number of Moves: " + algorithm.moves());
		System.out.println("Number of Nodes Processed: " + algorithm.expandedNodes());
		System.out.println("Running time: " + algorithm.getRunningTime() + " ms");
	}

	private class ShowThread extends Thread {

		public void run() {
			
			if (algorithm.isSolvable())
				for (Move a : algorithm.solution()) {
					if (a.getBlock() == 'X'){
						//Prints the board
						System.out.println(a.getBoard());
					}
					else if (a.getBlock() != '?'){
						System.out.println(a.getBoard());
					}
				}
			else
				System.out.println("The board you selected has no solution");
		}
	}

	
	public static void main(String[] args) throws InterruptedException {
		new ConsoleRunner();
	}
	
}
