/*
 * John Curran
 * 
 * The "Declare" Interpretation represents the declaration of a variable with
 * the given Interpretation as its value
 */

package Interpreter;

public class Declare implements Interpretation {
	public final String name;
	public final Interpretation i;
	
	public Declare(String n, Interpretation x) {
		name = n;
		i = x;
	}
	
	public short id() {
		return 8;
	}
	
	public String string() {
		return name + " = " + i.string();
	}
}
