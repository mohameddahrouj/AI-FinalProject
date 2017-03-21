package project;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import algorithms.AStarSolver;
import algorithms.BFSSolver;
import algorithms.SimulatedAnnealingSolver;

/**
 * Control Panel - RHS.
 */
@SuppressWarnings("serial")
public class RightPanel extends JPanel {

	private JComboBox<BoardFile> cbPuzzles;
	private JButton btnSolve;
	private JButton btnShow;
	private JRadioButton rbAStar;
	private JRadioButton rbSearchAnnealing;
	private JRadioButton rbBFS;
	private JLabel lbMoves;
	private JLabel lbExpNodes;
	private JLabel lbTime;
	private JLabel lbParkingConfig;
	private JLabel lbAlgorithms;
	private JLabel lbAnalytics;
	
	public static boolean solving = false;
	private Solver algorithm;
	
	/**
	 * Creates right side bar
	 */
	public RightPanel() {
		setPreferredSize(new Dimension(330,630));
		setBackground(Color.LIGHT_GRAY);
		setLayout(null);
		initializeComponents();
		loadPuzzles();
	}

	/**
	 * Initializes combobox, radiobuttons and button
	 */
	private void initializeComponents() {
		
		lbParkingConfig = new JLabel("Parking Configuration:");
		
		//Puzzle list
		cbPuzzles = new JComboBox<>();
		
		lbAlgorithms = new JLabel("Algorithms:");
		
		//Search algorithms
		rbAStar = new JRadioButton("A*");
		rbSearchAnnealing = new JRadioButton("Simulated Annealing");
		rbBFS = new JRadioButton("BFS");
		
		//Buttons
		btnSolve = new JButton("Solve");
		btnShow = new JButton("Show");
		
		lbAnalytics = new JLabel("Analytics:");
		
		//Analytics
		lbMoves = new JLabel("Number of Moves: N/A");
		lbExpNodes = new JLabel("Number of Expanded Nodes N/A");
		lbTime = new JLabel("Running time: N/A");
		
		lbParkingConfig.setBounds(50, 0, 200, 30);
		cbPuzzles.setBounds(50, 30, 200, 30);
		
		lbAlgorithms.setBounds(50, 60, 75, 50);
		rbAStar.setBounds(50, 95, 50, 30);
		rbBFS.setBounds(100, 95, 50, 30);
		rbSearchAnnealing.setBounds(160, 95, 200, 30);
		
		btnSolve.setBounds(50, 140, 100, 50);
		btnShow.setBounds(155, 140, 100, 50);
		
		lbAnalytics.setBounds(50, 190 , 100, 50);
		lbMoves.setBounds(50, 230, 300, 20);
		lbExpNodes.setBounds(50, 260, 300, 20);
		lbTime.setBounds(50, 290, 290, 20);
		
		cbPuzzles.setToolTipText("Choose a parking configuration to solve");
		cbPuzzles.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rbAStar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rbBFS.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rbSearchAnnealing.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnSolve.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnShow.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rbAStar.setFocusPainted(false);
		rbBFS.setFocusPainted(false);
		rbSearchAnnealing.setFocusPainted(false);
		btnSolve.setFocusPainted(false);
		btnShow.setFocusPainted(false);

		rbAStar.setBackground(Color.LIGHT_GRAY);
		rbBFS.setBackground(Color.LIGHT_GRAY);
		rbSearchAnnealing.setBackground(Color.LIGHT_GRAY);
		lbMoves.setBackground(Color.LIGHT_GRAY);
		lbExpNodes.setBackground(Color.LIGHT_GRAY);
		lbTime.setBackground(Color.LIGHT_GRAY);
		
		//Mutex
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(rbAStar);
		radioGroup.add(rbBFS);
		radioGroup.add(rbSearchAnnealing);

		//Add action listeners
		cbPuzzles.addActionListener(new ComboBoxListener());
		btnSolve.addActionListener(new SolveButtonListener());
		btnShow.addActionListener(new ShowButtonListener());

		btnShow.setEnabled(false);
		
		add(lbParkingConfig);
		add(cbPuzzles);
		add(lbAlgorithms);
		add(rbAStar);
		add(rbBFS);
		add(rbSearchAnnealing);
		add(btnSolve);
		add(btnShow);
		add(lbAnalytics);
		add(lbMoves);
		add(lbExpNodes);
		add(lbTime);
	}

	/**
	 * Loads possible parking configs from .puzzle files in /puzzles
	 */
	private void loadPuzzles() {
		File puzzlesDir = new File("puzzles/");
		for (File file : puzzlesDir.listFiles()) {
			String fileName = file.getName();
			if (Pattern.matches(".*(\\.puzzle)$", fileName))
				cbPuzzles.addItem(new BoardFile(file));
		}
	}

	//Parking config action listener
	private class ComboBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				Shared.board.load((BoardFile)cbPuzzles.getSelectedItem());
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}

	//Solve button action listener
	private class SolveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
				new SolverThread().start();
		}
	}
	
	//Solution action listener
	private class ShowButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				Shared.board.load((BoardFile)cbPuzzles.getSelectedItem());
				new ShowThread().start();
			} catch (FileNotFoundException e1) {}
		}
	}
	
	private class SolverThread extends Thread {

		public void run() {
			btnSolve.setEnabled(false);
			btnShow.setEnabled(false);
			cbPuzzles.setEnabled(false);
			rbAStar.setEnabled(false);
			rbBFS.setEnabled(false);
			rbSearchAnnealing.setEnabled(false);
			
			BoardFile bFile= (BoardFile)cbPuzzles.getSelectedItem();
			Scanner in = null;
			try {
				in = new Scanner(bFile.file());
			} catch (FileNotFoundException e1) {}

			in.next();
			char[][] blocks = new char[6][6];
			for (int i = 0; i < 6; i++)
				for (int j = 0; j < 6; j++)
					blocks[i][j] = in.next().charAt(0);

			Board initial = new Board(blocks);
			
			if (rbAStar.isSelected())
				algorithm = new AStarSolver(initial);
			else if (rbBFS.isSelected())
				algorithm = new BFSSolver(initial);
			else if (rbSearchAnnealing.isSelected())
				algorithm = new SimulatedAnnealingSolver(initial);
			else {
				JOptionPane.showMessageDialog(null, "Please select a search algorithm!", "Warning", JOptionPane.WARNING_MESSAGE);
				solving = false;
				
				btnSolve.setEnabled(true);
				btnShow.setEnabled(true);
				cbPuzzles.setEnabled(true);
				rbAStar.setEnabled(true);
				rbBFS.setEnabled(true);
				rbSearchAnnealing.setEnabled(true);
				return;
			}
			
			lbMoves.setText("Number of Moves: " + algorithm.moves());
			lbExpNodes.setText("Number of Nodes Processed: " + algorithm.expandedNodes());
			lbTime.setText("Running time: " + algorithm.getRunningTime() + " ms");
			
			btnSolve.setEnabled(true);
			btnShow.setEnabled(true);
			cbPuzzles.setEnabled(true);
			rbAStar.setEnabled(true);
			rbBFS.setEnabled(true);
			rbSearchAnnealing.setEnabled(true);
		}
	}

	private class ShowThread extends Thread {

		public void run() {
			btnSolve.setEnabled(false);
			btnShow.setEnabled(false);
			cbPuzzles.setEnabled(false);
			rbAStar.setEnabled(false);
			rbBFS.setEnabled(false);
			rbSearchAnnealing.setEnabled(false);
			
			if (algorithm.isSolvable())
				for (Action a : algorithm.solution()) {
					if (a.getBlock() == 'X'){
						Shared.board.blocks[0].move(100 * a.getMoves());
						//Prints the board
						System.out.println(a.getBoard());
					}
					else if (a.getBlock() != '?'){
						Shared.board.blocks[a.getBlock() - 96].move(100 * a.getMoves());
						System.out.println(a.getBoard());
					}
				}
			else
				JOptionPane.showMessageDialog(null, "The board you selected has no solution");
			btnSolve.setEnabled(true);
			btnShow.setEnabled(true);
			cbPuzzles.setEnabled(true);
			rbAStar.setEnabled(true);
			rbBFS.setEnabled(true);
			rbSearchAnnealing.setEnabled(true);
			
			Shared.board.blocks[0].setBackground(Color.GREEN);
		}
	}
}
