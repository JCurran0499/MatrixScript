/*
 * John Curran
 * 
 * The "Sub" Interpretation represents the subtraction of one Interpretation
 * object from another
 */

package Interpreter;

public class Sub implements Interpretation {
	public final Interpretation i1;
	public final Interpretation i2;
	
	public Sub(Interpretation x, Interpretation y) {
		i1 = x;
		i2 = y;
	}
	
	public short id() {
		return 3;
	}
	
	public String string() {
		return i1.string() + " - " + i2.string();
	}
}
