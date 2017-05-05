import java.awt.event.*;
/**
 * Calculations behind the scenes of the displayed result.
 * @author Daniel Chan
 */
public class Base4CalcState {
	
	/**
	 * result storage after operations are performed
	 */
	private int value;
	
	/**
	 * radix of current calculation as indicated by {@link Base4Panel#baseSelector}
	 */
	private int radix;
	private int prevRadix;
	private char prevOp;
	private char equaledOp;
	private int recentOperand;
	
	private String prevOperand;
	
	Base4CalcState() { 
		value = 0; 
		radix = 10;
		prevRadix = 10;
		prevOp = 'n';
		recentOperand = 0;
		prevOperand = "";
	}

	void clear() {
		value = 0;
		prevOp = 'n';
		recentOperand = 0;
		equaledOp = 'n';
		prevOperand = "";
	}
	
	int getValue() { return value; }
	void setValue(int n) { value = n; }

	void add(int n) { value += n; }
	
	void minus(int n) { value -= n; }
	
	void times(int n) { value *= n; }
	
	void divide(int n) {value /= n; }	
	
	public int getRadix() {
		return radix;
	}
	public void setRadix(int radix) {
		this.radix = radix;
	}
	
	/**
	 * Symbol used as a control for <code>JButton</code>
	 * operations to act upon.
	 * @return the previous operator
	 */
	public char getPrevOp() {
		return prevOp;
	}

	/**
	 * {@link Base4CalcState#getPrevOp()}
	 * @param operator the previous operator
	 */
	public void setPrevOp(char operator) {
		this.prevOp = operator;
	}

	/**
	 * Symbol used to retain operator for
	 * consecutive <code>equal</code> clicks
	 * @return the recently used operator
	 */
	public char getEqualedOp() {
		return equaledOp;
	}
	/**
	 * {@link Base4CalcState#getEqualedOp()}
	 * @param equaledOp the equaled operator
	 */
	public void setEqualedOp(char equaledOp) {
		this.equaledOp = equaledOp;
	}
	/**
	 * Value of the operand preceding the operation,
	 * to complete the operation. 
	 * @return the recent operand
	 */
	public int getRecentOperand() {
		return recentOperand;
	}
	/**
	 * {@link Base4CalcState#getRecentOperand()}
	 * @param recentOperand the recent operand
	 */
	public void setRecentOperand(int recentOperand) {
		this.recentOperand = recentOperand;
	}
	

	public void setRecentOperand(String recentOperand, int radix) {
		int temp = Integer.parseInt(recentOperand, prevRadix);
		Integer.toString(temp, radix);
	}
	
	/**
	 * Text representation of the previous operand
	 * for <code>JSlider baseSelector</code> to update
	 * to the selected base.
	 * 
	 * @return the previous operand
	 */
	public String getPrevOperand() {
		return prevOperand;
	}
	/**
	 * {@link Base4CalcState#getPrevOperand()}
	 * @param prevOperand the previous operand
	 */
	public void setPrevOperand(String prevOperand) {
		this.prevOperand = prevOperand;
	}
	/**
	 * The radix before the base change, used to interpret
	 * the previous displayed value as it's previous radix
	 * @return the previous radix
	 */
	public int getPrevRadix() {
		return prevRadix;
	}
	/**
	 * {@link Base4CalcState#getPrevRadix()}
	 * @param prevRadix the previous radix
	 */
	public void setPrevRadix(int prevRadix) {
		this.prevRadix = prevRadix;
	}

	
	
	
	
}
