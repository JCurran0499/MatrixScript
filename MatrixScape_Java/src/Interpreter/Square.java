/*
 * John Curran
 * 
 * The "Square" Interpretation represents whether a matrix is square
 */

package Interpreter;

public class Square implements Interpretation {
	public final Interpretation i;
	
	public Square(Interpretation x) {
		i = x;
	}
	
	public short id() {
		return 14;
	}
	
	public String string() {
		return "square? " + i.string();
	}
}
