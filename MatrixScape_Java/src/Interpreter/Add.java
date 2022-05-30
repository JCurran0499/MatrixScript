/*
 * John Curran
 * 
 * The "Add" Interpretation represents addition between two Interpretation objects
 */

package Interpreter;

public class Add implements Interpretation {
	public final Interpretation i1;
	public final Interpretation i2;
	
	public Add(Interpretation x, Interpretation y) {
		i1 = x;
		i2 = y;
	}
	
	public short id() {
		return 2;
	}
	
	public String string() {
		return i1.string() + " + " + i2.string();
	}
}
