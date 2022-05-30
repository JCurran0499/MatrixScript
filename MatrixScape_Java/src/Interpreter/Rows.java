/*
 * John Curran
 * 
 * The "Rows" Interpretation represents a request for the number of rows in a matrix
 */

package Interpreter;

public class Rows implements Interpretation {
	public final Interpretation i;
	
	public Rows(Interpretation x) {
		i = x;
	}
	
	public short id() {
		return 11;
	}
	
	public String string() {
		return "rows " + i.string();
	}
}
