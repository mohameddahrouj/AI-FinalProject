package project;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * Runner class
 * Contains main for executing application
 * @author Mohamed Dahrouj
 *
 */
@SuppressWarnings("serial")
public class AppRunner extends JFrame {
	
	public AppRunner() {
		AppComponents.appRunner = this;
		setTitle("COMP 4106- Parking Optimization");
		setSize(new Dimension(1000, 720));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(true);
		setLayout(new BorderLayout());
		
		ParkingPanel board = new ParkingPanel();
		AppComponents.board = board;
		ControlPanel controlSidebar = new ControlPanel();

		getContentPane().add(controlSidebar, BorderLayout.EAST);
		getContentPane().add(board, BorderLayout.CENTER);
		setVisible(true);
	}
	
	public static void main(String[] args) throws InterruptedException {
		new AppRunner();
	}
	
}
