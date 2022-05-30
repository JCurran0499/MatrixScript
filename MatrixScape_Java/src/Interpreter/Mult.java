/*
 * John Curran
 * 
 * The "Mult" Interpretation represents the multiplication of two Interpretation objects
 */

package Interpreter;

public class Mult implements Interpretation {
	public final Interpretation i1;
	public final Interpretation i2;
	
	public Mult(Interpretation x, Interpretation y) {
		i1 = x;
		i2 = y;
	}
	
	public short id() {
		return 4;
	}
	
	public String string() {
		return i1.string() + " * " + i2.string();
	}
}
