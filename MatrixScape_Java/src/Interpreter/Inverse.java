/*
 * John Curran
 * 
 * The "Inverse" Interpretation represents the inverse of a matrix
 */

package Interpreter;

public class Inverse implements Interpretation {
	public final Interpretation i;
	
	public Inverse(Interpretation x) {
		i = x;
	}
	
	public short id() {
		return 23;
	}
	
	public String string() {
		return "inverse " + i.string();
	}
}
