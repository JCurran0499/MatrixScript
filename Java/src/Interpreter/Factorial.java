/*
 * John Curran
 * 
 * The "Factorial" Interpretation represents a factorial expression
 */

package Interpreter;

public class Factorial implements Interpretation {
	public final Interpretation i;
	
	public Factorial(Interpretation x) {
		i = x;
	}
	
	public short id() {
		return 31;
	}
	
	public String string() {
		return i.string() + "!";
	}
}
