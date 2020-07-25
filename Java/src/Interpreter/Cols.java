/*
 * John Curran
 * 
 * The "Cols" Interpretation represents a request for the number of columns in a matrix
 */

package Interpreter;

public class Cols implements Interpretation {
	public final Interpretation i;
	
	public Cols(Interpretation x) {
		i = x;
	}
	
	public short id() {
		return 12;
	}
	
	public String string() {
		return "cols " + i.string();
	}
}
