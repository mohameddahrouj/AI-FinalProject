package project;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Vehicle extends JPanel {

	private int x; 
	private int y;
	private final int width;
	private final int height;
	private boolean orientation;
	public static final boolean HORIZONTAL = true;
	public static final boolean VERTICAL   = false;
	private JLabel label;

	/**
	 * Creates a GUI car (block)
	 * @param x	The x coordinate where the block is.
	 * @param y	The y coordinate where the block is.
	 * @param length The size of the block (2, 3 mainly).
	 * @param orientation The orientation of the block (Horizontal or Vertical).
	 * @param c	The character associated to this block.
	 */
	public Vehicle(int x, int y, int length, boolean orientation, char c) {
		this.x = x;
		this.y = y;
		this.orientation = orientation;
		this.width = (orientation == HORIZONTAL)? 100 * length - 1 : 100 - 1;
		this.height = (orientation == HORIZONTAL)? 100 - 1 : 100 * length - 1;
		setBounds(x, y, width, height);
		setPreferredSize(new Dimension(width, height));
		setBackground(new Color(random(), random(), random()));
		label = new JLabel(c+"");
		add(label);
	}

	/**
	 * Sets the car color
	 * @param color	The new color.
	 */
	public void setColor(Color color) {
		setBackground(color);
		label.setBackground(color);
	}

	/**
	 * Generates a random number for color
	 * @return	Random number integer in range between 0 and 255
	 */
	private int random() {
		return (int)(Math.random() * 256);
	}

	/**
	 * Move the car
	 * @param Pixels to be moved (positive -> up, right, negative -> left, bottom)
	 */
	public void move(int d) {
		try {
			if (orientation == HORIZONTAL)
				for (int i = 0; i <= Math.abs(d); i++) {
					setBounds(x + ((d < 0)? -i : i), y, width, height);
					// Sleep allows a realistic transition time to visualize
					Thread.sleep(5);
				}
			else
				for (int i = 0; i <= Math.abs(d); i++) {
					setBounds(x, y + ((d < 0)? -i : i), width, height);
					Thread.sleep(5);
				}
		x = getX();
		y = getY();
		} catch(InterruptedException e) {}
	}

}
