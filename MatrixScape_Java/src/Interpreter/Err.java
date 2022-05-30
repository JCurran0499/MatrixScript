/*
 * John Curran
 * 
 * The "Err" Interpretation represents an error in the MatrixScape command
 */

package Interpreter;

public class Err implements Interpretation {
	public final String err;
	
	public Err(String e) {
		err = e;
	}
	
	public short id() {
		return -1;
	}
	
	public String string() {
		return "Error: " + err;
	}
}
