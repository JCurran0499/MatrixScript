/*
 * John Curran
 * 
 * The "Logic" Interpretation represents the logical operators and, or, and not.
 * The specific operator being used is denoted by the 'operator' field
 */

package Interpreter;

public class Logic implements Interpretation {

	public final String operator;
	public final Interpretation i1;
	public final Interpretation i2;
	
	public Logic(String o, Interpretation x, Interpretation y) {
		operator = o;
		i1 = x;
		i2 = y;
	}
	
	public short id() {
		return 30;
	}
	
	public String string() {
		if (operator.equals("not"))
			return "not " + i1.string();
		else
			return i1.string() + " " + operator + " " + i2.string();
	}

}
