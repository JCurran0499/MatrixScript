/*
 * John Curran
 * 
 * The "Invertible" Interpretation represents whether a matrix is invertible
 */

package Interpreter;

public class Invertible implements Interpretation {
	public final Interpretation i;
	
	public Invertible(Interpretation x) {
		i = x;
	}
	
	public short id() {
		return 22;
	}
	
	public String string() {
		return "invertible? " + i.string();
	}
}
