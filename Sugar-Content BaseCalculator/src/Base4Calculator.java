import javax.swing.JFrame;
import javax.swing.*;
/**
 * Handles program execution.
 * @author Daniel Chan
 */
public class Base4Calculator {

	public static void main(String[] args) {
		createAndShowGUI();
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Base 2-16 Calculator");
		frame.add(new Base4Panel());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 600);
		frame.setVisible(true);
	}
}
