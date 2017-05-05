import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Modified JPanel in the likeness and behavior of a calculator.
 */
public class Base4Panel extends JPanel {
	
	private
	Base4CalcState calc; // this object will actually do the calculating work
	/**
	 * Collected digit buttons.
	 */
	private ArrayList<JButton> numberButtons;
	JButton plus, 
	minus,
	times,
	divide,
	equal,
	clear,
	negate,
	currentValue;
	private String[] numberButtonNames= {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
		"A", "B", "C", "D", "E", "F"};
	private JTextField display;
	/**
	 * adjusts the base used in current calculation
	 */
	private JSlider baseSelector;
	// converts the held value to its base ten equivalent in String type, in order to 
	// assist in updating when changing bases.
	private String valueBaseTen;
	// converts the held string to its base ten equivalent in int type, in order to 
	// assist in updating when changing bases.
	private int operandBaseTen;
	// to hold the base ten equivalent, assists in recalling the 
	// last operand used after an equals operation
	private String recentOperandBaseTen;
	// if the last button hit was an operator, and the next button hit is a digit
	// start a new operand
	private boolean operatorHit = false;
	// if the last button hit was a digit, and the next button hit is an operator,
	// perform the behavior set for the operation button
	// stops operators, with the exception of equals, from being pushed twice
	private boolean digitHit = false;  

	/**
	 * a GUI representation of a multi-base calculator. Uses a <code>GridBagLayout</code>.
	 */
	public
	Base4Panel() {
		this.setLayout(new GridBagLayout()); 
		GridBagConstraints c = new GridBagConstraints();
		
		calc = new Base4CalcState();
		display = new JTextField();
		display.setEditable(false);
		display.setHorizontalAlignment(JTextField.RIGHT);
		baseSelector = new JSlider(2,16,10);
		baseSelector.setMajorTickSpacing(2);
		baseSelector.setMinorTickSpacing(1);
		baseSelector.setPaintTicks(true);
		baseSelector.setPaintLabels(true);
		numberButtons = new ArrayList<JButton>(16);
		for (int i = 0; i < numberButtonNames.length; i++) {
			numberButtons.add(new JButton(numberButtonNames[i]));
			numberButtons.get(i).addActionListener(new NumberListener());
		}
		for (int i = 10; i < 16; i++) {
			numberButtons.get(i).setEnabled(false);
		}
		
		plus = new JButton("+"); 
		clear = new JButton("Clear");
		equal = new JButton("=");
		minus = new JButton("-"); 
		times = new JButton("x"); 
		divide = new JButton("/");
		negate = new JButton("+-");
		currentValue = new JButton("->");
		currentValue.setEnabled(false);
		
		currentValue.addActionListener(new CurrentValueListener());
		plus.addActionListener(new PlusListener());
		minus.addActionListener(new MinusListener());
		times.addActionListener(new TimesListener());
		divide.addActionListener(new DivideListener());
		equal.addActionListener(new EqualListener());
		clear.addActionListener(new ClearListener());
		negate.addActionListener(new NegateListener());
		baseSelector.addChangeListener(new BaseListener());

		c.fill = GridBagConstraints.BOTH;
		c.weightx = .8;
		c.weighty = .8;
		c.insets = new Insets(2, 2, 2, 2);
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 0;
		
		add(display, c);
		
		c.gridwidth = 1;
		c.gridy = 1;
		add(numberButtons.get(13), c);
		c.gridx = 1;
		add(numberButtons.get(14), c);
		c.gridx = 2;
		add(numberButtons.get(15), c);
		c.gridx = 3;
		add(clear, c);
		
		c.gridy = 2;
		c.gridx = 0;
		add(numberButtons.get(10), c);
		c.gridx = 1;
		add(numberButtons.get(11), c);
		c.gridx = 2;
		add(numberButtons.get(12), c);
		c.gridx = 3;
		add(divide, c);
		
		c.gridy = 3;
		c.gridx = 0;
		add(numberButtons.get(7), c);
		c.gridx = 1;
		add(numberButtons.get(8), c);
		c.gridx = 2;
		add(numberButtons.get(9), c);
		c.gridx = 3;
		add(times, c);
		
		c.gridy = 4;
		c.gridx = 0;
		add(numberButtons.get(4), c);
		c.gridx = 1;
		add(numberButtons.get(5), c);
		c.gridx = 2;
		add(numberButtons.get(6), c);
		c.gridx = 3;
		add(minus, c);
		
		c.gridy = 5;
		c.gridx = 0;
		add(numberButtons.get(1), c);
		c.gridx = 1;
		add(numberButtons.get(2), c);
		c.gridx = 2;
		add(numberButtons.get(3), c);
		c.gridx = 3;
		add(plus, c);
		
		c.gridy = 6;
		c.gridx = 0;
		add(negate, c);
		c.gridx = 1;
		add(numberButtons.get(0), c);
		c.gridx = 2;
		add(currentValue, c);
		c.gridx = 3;
		add(equal, c);
		
		c.gridwidth = 4;
		c.gridy = 7;
		c.gridx = 0;
		add(baseSelector, c);
	}
	
	/**
	 * 	Updates the base to accommodate for the desired base representations.
	 * <p>Corresponds with <code>JSlider</code> {@link Base4Panel#baseSelector}
	 * 
	 * @author Daniel Chan
	 */
	class BaseListener implements ChangeListener  {
		/**
		 * Invoked when the target of the listener has changed its state.
		 * <p> Updates available button selections with corresponding base 
		 * changes. Updates the displayed result with the desired base representation.
		 * Updates entered operands to desired base change, as well as previously
		 * entered values.
		 * 
		 * @param event a ChangeEvent object
		 */
		public void stateChanged(ChangeEvent event) {
			switch (baseSelector.getValue()) {
				case 2 :
					calc.setRadix(2);
					break;
				case 3 :
					calc.setRadix(3);
					break;
				case 4 :
					calc.setRadix(4);
					break;
				case 5 :
					calc.setRadix(5);
					break;
				case 6 :
					calc.setRadix(6);
					break;
				case 7 :
					calc.setRadix(7);
					break;
				case 8 :
					calc.setRadix(8);
					break;
				case 9 :
					calc.setRadix(9);
					break;
				case 10 :
					calc.setRadix(10);
					break;
				case 11 :
					calc.setRadix(11);
					break;
				case 12 :
					calc.setRadix(12);
					break;
				case 13 :
					calc.setRadix(13);
					break;
				case 14 :
					calc.setRadix(14);
					break;
				case 15 :
					calc.setRadix(15);
					break;
				case 16 :
					calc.setRadix(16);
					break;
			}
			
			for (int i = 0; i < calc.getRadix(); i++) {
				numberButtons.get(i).setEnabled(true);
			}
			for (int j = calc.getRadix(); j < baseSelector.getMaximum(); j++) {
				numberButtons.get(j).setEnabled(false);
			}
			// as base changes, values in calc state changes
			if (calc.getPrevOp() == '=') {
				display.setText(Integer.toString(calc.getValue(), calc.getRadix()).toUpperCase());
			} else {
				operandBaseTen = Integer.parseInt(calc.getPrevOperand(), calc.getPrevRadix());
				display.setText(Integer.toString(operandBaseTen, calc.getRadix()).toUpperCase());
				valueBaseTen = Integer.toString(calc.getValue(), 10);
				calc.setValue(Integer.parseInt(valueBaseTen, calc.getRadix()));
			}	
		}
	}

	/**
	 *  Displays the result held in the calculator as its base ten equivalent.
	 * <p> Corresponds with <code>JButton</code> {@link Base4Panel#currentValue}
	 * 
	 * @author Daniel Chan
	 *
	 */
	class CurrentValueListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			
			display.setText(Integer.toString(calc.getValue()));
		}
	}
	
	/**
	 * Handles operand components of a calculation.
	 * <p>Corresponds with <code>ArrayList<</code><code>JButton></code> {@link Base4Panel#numberButtons}
	 * 
	 * @author Daniel Chan
	 */
	class NumberListener implements ActionListener {
		/**
		 * Invoked by occurrence of digit action. 
		 * <p>Displays the desired digit inputs. Prepares and retains 
		 * information about the operand in case of desired base change.
		 * 
		 * @param event an ActionEvent object
		 */
		public void actionPerformed(ActionEvent event) {
			currentValue.setEnabled(false);
			switch (event.getActionCommand()) {
				case "0" :
					if (calc.getPrevOp() == '=') {
						display.setText("0");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) {
						display.setText("0");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "0");}
					calc.setPrevOperand(calc.getPrevOperand() + "0");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "1" : 
					if (calc.getPrevOp() == '=') {
						display.setText("1");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) {
						display.setText("1");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "1");}
					calc.setPrevOperand(calc.getPrevOperand() + "1");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "2" :
					if (calc.getPrevOp() == '=') {
						display.setText("2");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) {
						display.setText("2");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "2");}
					calc.setPrevOperand(calc.getPrevOperand() + "2");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "3" :
					if (calc.getPrevOp() == '=') {
						display.setText("3");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) { 
						display.setText("3");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "3");}
					calc.setPrevOperand(calc.getPrevOperand() + "3");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "4" :
					if (calc.getPrevOp() == '=') {
						display.setText("4");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) { 
						display.setText("4");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "4");}
					calc.setPrevOperand(calc.getPrevOperand() + "4");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "5" :
					if (calc.getPrevOp() == '=') {
						display.setText("5");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) { 
						display.setText("5");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "5");}
					calc.setPrevOperand(calc.getPrevOperand() + "5");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "6" :
					if (calc.getPrevOp() == '=') {
						display.setText("6");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) { 
						display.setText("6");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "6");}
					calc.setPrevOperand(calc.getPrevOperand() + "6");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "7" :
					if (calc.getPrevOp() == '=') {
						display.setText("7");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) { 
						display.setText("7");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "7");}
					calc.setPrevOperand(calc.getPrevOperand() + "7");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "8" :
					if (calc.getPrevOp() == '=') {
						display.setText("8");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) { 
						display.setText("8");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "8");}
					calc.setPrevOperand(calc.getPrevOperand() + "8");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "9" :
					if (calc.getPrevOp() == '=') {
						display.setText("9");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) { 
						display.setText("9");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "9");}
					calc.setPrevOperand(calc.getPrevOperand() + "9");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "A" :
					if (calc.getPrevOp() == '=') {
						display.setText("A");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) { 
						display.setText("A");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "A");}
					calc.setPrevOperand(calc.getPrevOperand() + "A");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "B" :
					if (calc.getPrevOp() == '=') {
						display.setText("B");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) { 
						display.setText("B");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "B");}
					calc.setPrevOperand(calc.getPrevOperand() + "B");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "C" :
					if (calc.getPrevOp() == '=') {
						display.setText("C");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) { 
						display.setText("C");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "C");}
					calc.setPrevOperand(calc.getPrevOperand() + "C");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "D" :
					if (calc.getPrevOp() == '=') {
						display.setText("D");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) { 
						display.setText("D");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "D");}
					calc.setPrevOperand(calc.getPrevOperand() + "D");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "E" :
					if (calc.getPrevOp() == '=') {
						display.setText("E");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) { 
						display.setText("E");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "E");}
					calc.setPrevOperand(calc.getPrevOperand() + "E");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
				case "F" :
					if (calc.getPrevOp() == '=') {
						display.setText("F");
						calc.setPrevOp('n');
						calc.setValue(0);
						operatorHit = false;
					} else if (operatorHit) { 
						display.setText("F");
						calc.setPrevOperand("");
						operatorHit = false;
					} else {display.setText(display.getText() + "F");}
					calc.setPrevOperand(calc.getPrevOperand() + "F");
					calc.setPrevRadix(calc.getRadix());
					digitHit = true;
					break;
			}
		}
		
	}
	/**
	 * Updates the displayed result to be the negative of it.
	 * <p>Corresponds with <code>JButton</code> {@link Base4Panel#negate}
	 * 
	 * @author Daniel Chan
	 */
	class NegateListener implements ActionListener {
		/**
		 * Invoked by occurrence of negation.
		 * <p>Initiates negative values for calculation.
		 * 
		 * @param event an ActionEvent object
		 */
		public void actionPerformed(ActionEvent event) {
			if (calc.getPrevOp() == '=') { 
				calc.times(-1);
				display.setText(Integer.toString(calc.getValue()));
			} else {
				calc.setRecentOperand(Integer.parseInt(display.getText(), calc.getRadix()));
				int signedInteger = calc.getRecentOperand() * (-1);
				display.setText(Integer.toString(signedInteger, calc.getRadix()));
				calc.setRecentOperand(0);
			}
		}
	}
	/**
	 * Resets state of calculator to conditions at beginning of run time,
	 * minus the base representation controlled by <code>JSlider baseSelector</code>
	 * <p>Corresponds with <code>JButton</code> {@link Base4Panel#clear}
	 * 
	 * @author Daniel Chan
	 */
	class ClearListener implements ActionListener {
		/**
		 * Invoked by reset of calculations.
		 * <p>Sets {@link Base4CalcState} member variables to null equivalents.
		 * Sets button interactions to <code>false</code>.
		 * 
		 * @param an ActionEvent object
		 */
		public void actionPerformed(ActionEvent event) {
			calc.clear();
			display.setText("");
			operatorHit = false;
			digitHit = false;
		}
	}
	/**
	 * Updates by adding the following value to the result.
	 * <p>Updates current calculation to be added to if a
	 * result has been displayed.
	 * 
	 * @author Daniel Chan
	 */
	class PlusListener implements ActionListener {
		/**
		 * Completes the previous operation and makes
		 * addition the pending operation.
		 * 
		 * @param event an ActionEvent object
		 */
		public void actionPerformed(ActionEvent event) {
			if (digitHit) {
				digitHit = false;
				operatorHit = true;
				calc.setRecentOperand(0);
				
				// don't do anything yet until another operand has been entered
				if (calc.getPrevOp() == '=') {
					calc.setPrevOp('+');
					return;
				}
				
				switch(calc.getPrevOp()) {
					case '-' :
						calc.minus(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('+');
						break;
					case '*' :
						calc.times(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('+');
						break;
					case '/' :
						calc.divide(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('+');
					break;
					default : 
						calc.add(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('+');
				}
			}
		}
	}
	/**
	 * Updates by initiating a subtraction operation, 
	 * the following value subtracted from the preceding
	 * inputs.
	 * 
	 * @author Daniel Chan
	 */
	class MinusListener implements ActionListener {
		/**
		 * Completes the previous operation and makes subtraction
		 * the pending operation.
		 * 
		 * @param event an ActionEvent object
		 */
		public void actionPerformed(ActionEvent event) {
			if (digitHit) {
				digitHit = false;
				operatorHit = true;
				calc.setRecentOperand(0);
				
				if (calc.getPrevOp() == '=') {
					calc.setPrevOp('-');
					return;
				}
				
				switch(calc.getPrevOp()) {
					case '+' : 
						calc.add(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('-');
						break;
					case '*' :
						calc.times(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('-');
						break;
					case '/' :
						calc.divide(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('-');
						break;
					case 'n' :
						calc.setValue(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('-');
						break;
					default : 
						calc.minus(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('-');
						break;
				}	
			}
		}
	}

	/**
	 * Updates by initiating a multiplication operation,
	 * the following value multiplied by the preceding
	 * inputs.
	 * 
	 * @author Daniel Chan
	 */
	class TimesListener implements ActionListener {
		/**
		 * Completes the previous operation and makes multiplication
		 * the pending operation.
		 * 
		 * @param event an ActionEvent object
		 */
		public void actionPerformed(ActionEvent event) {
			if (digitHit) {
				digitHit = false;
				operatorHit = true;
				calc.setRecentOperand(0);
				
				if (calc.getPrevOp() == '=') {
					calc.setPrevOp('*');
					return;
				}
				
				switch(calc.getPrevOp()) {
					case '+' : 
						calc.add(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('*');
						break;
					case '-' :
						calc.minus(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('*');
						break;
					case '/' :
						calc.divide(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('*');
						break;
					default : 
						// initial value = 0, times does value * the digits already entered
						if (calc.getValue() == 0) {
							calc.setValue(1);
						}
						calc.times(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('*');
				}
			}
		}
	}
	/**
	 * Updates by initiating a division operation,
	 * the preceding inputs divided by the following value.
	 * 
	 * @author Daniel Chan
	 */
	class DivideListener implements ActionListener {
		/**
		 * Completes the previous operation and makes division
		 * the pending operation.
		 * 
		 * @param event an ActionEvent object
		 */
		public void actionPerformed(ActionEvent event) {
			if (digitHit) {
				digitHit = false;
				operatorHit = true;
				calc.setRecentOperand(0);
				
				if (calc.getPrevOp() == '=') {
					calc.setPrevOp('/');
					return;
				}
				
				switch(calc.getPrevOp()) {
					case '+' : 
						calc.add(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('/');
						break;
					case '-' :
						calc.minus(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('/');
						break;
					case '*' :
						calc.times(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('/');
						break;
					default : 
						// "normal" division would always return 0
						if (calc.getValue() == 0) {
							calc.setValue(Integer.parseInt(display.getText(), calc.getRadix()));
							return;
						}
						calc.divide(Integer.parseInt(display.getText(), calc.getRadix()));
						display.setText(Integer.toString(calc.getValue(), calc.getRadix()));
						calc.setPrevOp('/');
				}
			}
		}
	}
	
	/** Updates by displaying the result.
	 * 
	 * @author Daniel Chan
	 */
	class EqualListener implements ActionListener {
		/**
		 * Completes the previous operation. 
		 * <p>Retains the most recent operation and operand in 
		 * its respective base to repeatedly calculate when
		 * invoking consecutive <code>JButton equal</code> actions.
		 * 
		 * @param event an ActionEvent object
		 */
		public void actionPerformed(ActionEvent event) {
			
			digitHit = true;
			operatorHit = true;
			// after hitting equals, do one of these cases
			switch (calc.getPrevOp()) {
				case '+' :
					if (calc.getRecentOperand() == 0) {
						calc.setRecentOperand(Integer.parseInt(display.getText(), calc.getRadix()));
						calc.setPrevRadix(calc.getRadix());
					}
					calc.setPrevOp('=');
					calc.setEqualedOp('+');
					calc.add(Integer.parseInt(display.getText(), calc.getRadix()));
					display.setText(Integer.toString(calc.getValue(), calc.getRadix()).toUpperCase());
					currentValue.setEnabled(true);
					return;
				
				case '-' :
					if (calc.getRecentOperand() == 0) {
						calc.setRecentOperand(Integer.parseInt(display.getText(), calc.getRadix()));
						calc.setPrevRadix(calc.getRadix());
					}
					calc.setPrevOp('=');
					calc.setEqualedOp('-');
					calc.minus(Integer.parseInt(display.getText(), calc.getRadix()));
					display.setText(Integer.toString(calc.getValue(), calc.getRadix()).toUpperCase());
					currentValue.setEnabled(true);
					return;
				
				case '*' :
					if (calc.getRecentOperand() == 0) {
						calc.setRecentOperand(Integer.parseInt(display.getText(), calc.getRadix()));
						calc.setPrevRadix(calc.getRadix());
					}
					calc.setPrevOp('=');
					calc.setEqualedOp('*');
					calc.times(Integer.parseInt(display.getText(), calc.getRadix()));
					display.setText(Integer.toString(calc.getValue(), calc.getRadix()).toUpperCase());
					currentValue.setEnabled(true);
					return;
				
				case '/' :
					if (calc.getRecentOperand() == 0) {
						calc.setRecentOperand(Integer.parseInt(display.getText(), calc.getRadix()));
						calc.setPrevRadix(calc.getRadix());
					}
					calc.setPrevOp('=');
					calc.setEqualedOp('/');
					calc.divide(Integer.parseInt(display.getText(), calc.getRadix()));
					display.setText(Integer.toString(calc.getValue(), calc.getRadix()).toUpperCase());	
					currentValue.setEnabled(true);
					return;
			}
			
			// after hitting equals again, do one of these operations
			if (calc.getPrevOp() == '=') {
				recentOperandBaseTen = Integer.toString(calc.getRecentOperand(), calc.getPrevRadix());
				calc.setRecentOperand(Integer.parseInt(recentOperandBaseTen, calc.getRadix()));
				calc.setPrevRadix(calc.getRadix());
				switch(calc.getEqualedOp()) {
				case '+' : 
					calc.add(calc.getRecentOperand());
					display.setText(Integer.toString(calc.getValue(), calc.getRadix()).toUpperCase());
					break;
				case '-' :
					calc.minus(calc.getRecentOperand());
					display.setText(Integer.toString(calc.getValue(), calc.getRadix()).toUpperCase());
					break;
				case '*' :
					calc.times(calc.getRecentOperand());
					display.setText(Integer.toString(calc.getValue(), calc.getRadix()).toUpperCase());
					break;
				case '/' : 
					calc.divide(calc.getRecentOperand());
					display.setText(Integer.toString(calc.getValue(), calc.getRadix()).toUpperCase());
					break;
				}
				currentValue.setEnabled(true);;
			}
		}
	}
}
