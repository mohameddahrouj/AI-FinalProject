package project;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class App extends JFrame {
	
	//Runner class
	public App() {
		//Initialize frame
		Shared.app = this;
		setTitle("COMP 4106- Parking Optimization");
		//Set the size of the frame
		setSize(new Dimension(970, 700));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new BorderLayout());
		
		//Initialize board panel
		BoardPanel board = new BoardPanel();
		Shared.board = board;
		//Initialize control panel
		RightPanel rightSidebar = new RightPanel();

		getContentPane().add(rightSidebar, BorderLayout.EAST);
		getContentPane().add(board, BorderLayout.CENTER);
		setVisible(true);
	}
	
	public static void main(String[] args) throws InterruptedException {
		new App();
	}
	
}
