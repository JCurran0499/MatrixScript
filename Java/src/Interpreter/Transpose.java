/*
 * John Curran
 * 
 * The "Transpose" Interpretation represents the transpose of a matrix
 */

package Interpreter;

public class Transpose implements Interpretation {
	public final Interpretation i;
	
	public Transpose(Interpretation x) {
		i = x;
	}
	
	public short id() {
		return 18;
	}
	
	public String string() {
		return "transpose " + i.string();
	}
}
