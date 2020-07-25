/*
 * John Curran
 * 
 * The "Var" Interpretation represents a variable
 */


package Interpreter;

public class Var implements Interpretation {
	
	public final String name;
	
	public Var(String n) {
		name = n;
	}
	
	public short id() {
		return 9;
	}
	
	public String string() {
		return name;
	}
}
