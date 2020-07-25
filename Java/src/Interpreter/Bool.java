/*
 * John Curran
 * 
 * The "Bool" Interpretation represents a boolean value
 */

package Interpreter;

public class Bool implements Interpretation {
	public final boolean i;
	
	public Bool(boolean b) {
		i = b;
	}
	
	public short id() {
		return 10;
	}
	
	public String string() {
		if (i) return "true";
		else return "false";
	}
}
