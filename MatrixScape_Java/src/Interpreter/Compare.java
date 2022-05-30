/*
 * John Curran
 * 
 * The "Compare" Interpretation represents any of the comparison operators.
 * This includes equals, not equals, greater than, less than, greater than
 * or equal to, and less than or equal to. The type of comparison operator
 * is determined by the 'operator' field
 */

package Interpreter;

public class Compare implements Interpretation {
	public final String operator;
	public final Interpretation i1;
	public final Interpretation i2;
	
	public Compare(String o, Interpretation x, Interpretation y) {
		operator = o;
		i1 = x;
		i2 = y;
	}
	
	public short id() {
		return 7;
	}
	
	public String string() {
		return i1.string() + " " + operator + " " + i2.string();
	}
}
