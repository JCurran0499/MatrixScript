/*
 * John Curran
 * 
 * The "List" Interpretation represents the MatrixScape list command,
 * which lists all declared variables in the program
 */

package Interpreter;

public class List implements Interpretation {
	public List() {}
	
	public short id() {
		return 29;
	}
	
	public String string() {
		return "list";
	}
}
