/*
 * John Curran
 * 
 * The "Pow" Interpretation represents an Interpretation object brought to
 * a given exponent
 */

package Interpreter;

public class Pow implements Interpretation {
	public final Interpretation i1;
	public final Interpretation i2;
	
	public Pow(Interpretation x, Interpretation y) {
		i1 = x;
		i2 = y;
	}
	
	public short id() {
		return 6;
	}
	
	public String string() {
		return i1.string() + " ^ " + i2.string();
	}
}
