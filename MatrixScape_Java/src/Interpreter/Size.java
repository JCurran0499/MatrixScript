/*
 * John Curran
 * 
 * The "Size" Interpretation represents the size of a matrix or tuple
 */

package Interpreter;

public class Size implements Interpretation {
	public final Interpretation i;
	
	public Size(Interpretation x) {
		i = x;
	}
	
	public short id() {
		return 13;
	}
	
	public String string() {
		return "size " + i.string();
	}
}
