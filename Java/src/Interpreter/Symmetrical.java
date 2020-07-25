/*
 * John Curran
 * 
 * The "Symmetrical" Interpretation represents whether a matrix is symmetrical
 */

package Interpreter;

public class Symmetrical implements Interpretation {
	public final Interpretation i;
	
	public Symmetrical(Interpretation x) {
		i = x;
	}
	
	public short id() {
		return 15;
	}
	
	public String string() {
		return "symmetrical? " + i.string();
	}
}
