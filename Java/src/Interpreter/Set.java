/*
 * John Curran
 * 
 * The "Set" Interpretation represents the set command, which updates/changes
 * values, rows, and/or columns in a matrix
 */

package Interpreter;

public class Set implements Interpretation {
	public final Interpretation i1;
	public final Interpretation i2;
	public final Interpretation i3;
	
	public Set(Interpretation x, Interpretation y, Interpretation z) {
		i1 = x;
		i2 = y;
		i3 = z;
	}
	
	public short id() {
		return 16;
	}
	
	public String string() {
		return "set " + i1.string() + " from " + i2.string() + " to " + i3.string();
	}
}
