package project;
import java.io.File;

/**
 * Object that represents a parking lot file
 * @author Mohamed Dahrouj
 *
 */
public class FileUtility {
	
	private File file;
	private String name;
	
	/**
	 * Creates a new Parking Lot File, saves a .parking File.
	 * @param f	.parking File
	 */
	public FileUtility(File f) {
		file = f;
		String[] nameParts = f.getName().split("-");
		name = nameParts[0] + " " + Integer.parseInt(nameParts[1].substring(0,2));
	}
	
	/**
	 * @return File associated to this BoardFile object.
	 */
	public File file() {
		return file;
	}
	
	// Name of file
	public String toString() {
		return name;
	}
	
}
