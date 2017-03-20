import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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

/**
 * Panel that contains the right sidebar (settings).
 */
@SuppressWarnings("serial")
public class RightPanel extends JPanel {

	private JComboBox<BoardFile> cbPuzzles;
	private JButton btnSolve;
	private JButton btnShow;
	private JRadioButton rbAStar;
	private JRadioButton rbDFS;
	private JRadioButton rbBFS;
	private JLabel lbMoves;
	private JLabel lbExpNodes;
	private JLabel lbTime;
	
	public static boolean solving = false;
	private Solver s;
	
	/**
	 * Creates right sidebar
	 */
	public RightPanel() {
		setPreferredSize(new Dimension(300,630));
		setBackground(Color.LIGHT_GRAY);
		setLayout(null);

		initComponents();
		loadPuzzles();
	}

	/**
	 * Initializes combobox, radiobuttons and button
	 */
	private void initComponents() {
		cbPuzzles = new JComboBox<>();
		rbAStar   = new JRadioButton("A*");
		rbDFS     = new JRadioButton("DFS");
		rbBFS 	  = new JRadioButton("BFS");
		btnSolve  = new JButton("Solve");
		btnShow  = new JButton("Show");
		lbMoves = new JLabel("");
		lbExpNodes = new JLabel("");
		lbTime = new JLabel("");
		
		cbPuzzles.setBounds(50, 0, 200, 30);
		rbAStar.setBounds(50, 50, 50, 50);
		rbBFS.setBounds(120, 50, 75, 50);
		rbDFS.setBounds(190, 50, 75, 50);
		btnSolve.setBounds(50, 140, 100, 50);
		btnShow.setBounds(155, 140, 100, 50);
		lbMoves.setBounds(50, 220, 300, 30);
		lbExpNodes.setBounds(50, 260, 300, 30);
		lbTime.setBounds(50, 300, 300, 30);
		
		cbPuzzles.setToolTipText("Pick a puzzle to solve");
		cbPuzzles.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rbAStar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rbBFS.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rbDFS.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnSolve.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnShow.setCursor(new Cursor(Cursor.HAND_CURSOR));
		rbAStar.setFocusPainted(false);
		rbBFS.setFocusPainted(false);
		rbDFS.setFocusPainted(false);
		btnSolve.setFocusPainted(false);
		btnShow.setFocusPainted(false);

		rbAStar.setBackground(Color.LIGHT_GRAY);
		rbBFS.setBackground(Color.LIGHT_GRAY);
		rbDFS.setBackground(Color.LIGHT_GRAY);
		lbMoves.setBackground(Color.LIGHT_GRAY);
		lbExpNodes.setBackground(Color.LIGHT_GRAY);
		lbTime.setBackground(Color.LIGHT_GRAY);
		
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(rbAStar);
		radioGroup.add(rbBFS);
		radioGroup.add(rbDFS);

		cbPuzzles.addActionListener(new ComboBoxListener());
		btnSolve.addActionListener(new SolveButtonListener());
		btnShow.addActionListener(new ShowButtonListener());

		btnShow.setEnabled(false);
		
		add(cbPuzzles);
		add(rbAStar);
		add(rbBFS);
		add(rbDFS);
		add(btnSolve);
		add(btnShow);
		add(lbMoves);
		add(lbExpNodes);
		add(lbTime);
	}

	/**
	 * Loads puzzles from .puzzle files in /puzzles
	 */
	private void loadPuzzles() {
		File puzzlesDir = new File("puzzles/");
		for (File file : puzzlesDir.listFiles()) {
			String fileName = file.getName();
			if (Pattern.matches(".*(\\.puzzle)$", fileName))
				cbPuzzles.addItem(new BoardFile(file));
		}
	}

	private class ComboBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				Shared.board.load((BoardFile)cbPuzzles.getSelectedItem());
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}

	private class SolveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
				new SolverThread().start();
		}
	}
	
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
			rbDFS.setEnabled(false);
			
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
				s = new AStarSolver(initial);
			else if (rbBFS.isSelected())
				s = new BFSSolver(initial);
			else if (rbDFS.isSelected())
				s = new DFSSolver(initial);
			else {
				JOptionPane.showMessageDialog(null,
						"You should select an algorithm");
				solving = false;
				
				lbMoves.setText("Number of Moves: N/A");
				lbExpNodes.setText("Number of Expanded Nodes N/A");
				lbTime.setText("Running time: N/A");
				
				btnSolve.setEnabled(true);
				btnShow.setEnabled(true);
				cbPuzzles.setEnabled(true);
				rbAStar.setEnabled(true);
				rbBFS.setEnabled(true);
				rbDFS.setEnabled(true);
				return;
			}
			
			lbMoves.setText("Number of Moves: " + s.moves());
			lbExpNodes.setText("Number of Expanded Nodes " + s.expandedNodes());
			lbTime.setText("Running time: " + s.getRunningTime());
			
			btnSolve.setEnabled(true);
			btnShow.setEnabled(true);
			cbPuzzles.setEnabled(true);
			rbAStar.setEnabled(true);
			rbBFS.setEnabled(true);
			rbDFS.setEnabled(true);
		}
	}

	private class ShowThread extends Thread {

		public void run() {
			btnSolve.setEnabled(false);
			btnShow.setEnabled(false);
			cbPuzzles.setEnabled(false);
			rbAStar.setEnabled(false);
			rbBFS.setEnabled(false);
			rbDFS.setEnabled(false);
			
			if (s.isSolvable())
				for (Action a : s.solution()) {
					if (a.getBlock() == 'X')
						Shared.board.blocks[0].move(100 * a.getMoves());
					else if (a.getBlock() != '?')
						Shared.board.blocks[a.getBlock() - 96].move(100 * a.getMoves());
				}
			else
				JOptionPane.showMessageDialog(null,
						"The board you selected has no solution");
			btnSolve.setEnabled(true);
			btnShow.setEnabled(true);
			cbPuzzles.setEnabled(true);
			rbAStar.setEnabled(true);
			rbBFS.setEnabled(true);
			rbDFS.setEnabled(true);
			
			Shared.board.blocks[0].setBackground(Color.GREEN);
		}
	}
}
