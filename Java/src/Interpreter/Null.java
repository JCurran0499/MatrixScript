/*
 * John Curran
 * 
 * The "Null" Interpretation represents an empty object. This Interpretation
 * is returned when the command holds no information
 */

package Interpreter;

public class Null implements Interpretation {

	public Null() {}
	
	public short id() {
		return 24;
	}
	
	public String string() {
		return "";
	}
}
