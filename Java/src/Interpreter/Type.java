/*
 * John Curran
 * 
 * The "Type" Interpretation represents the type command, which returns 
 * the data type of the field
 */

package Interpreter;

public class Type implements Interpretation {
	public final Interpretation i;
	
	public Type(Interpretation x) {
		i = x;
	}
	
	public short id() {
		return 28;
	}
	
	public String string() {
		if (i.id() == 0)
			return "matrix";
		if (i.id() == 1)
			return "number";
		if (i.id() == 10)
			return "boolean";
		if (i.id() == 25)
			return "tuple";
		if (i.id() == 26)
			return "range";
		else
			return "void";
	}
}
