/*
 * John Curran
 * 
 * The "Rref" Interpretation represents the reduced row echelon form of a matrix
 */

package Interpreter;

public class Rref implements Interpretation {
	public final Interpretation i;
	
	public Rref(Interpretation x) {
		i = x;
	}
	
	public short id() {
		return 19;
	}
	
	public String string() {
		return "rref " + i.string();
	}
}
