/*
 * John Curran
 * 
 * The "Get" Interpretation represents a MatrixScape Get request
 */

package Interpreter;

public class Get implements Interpretation {
	public final Interpretation i1;
	public final Interpretation i2;
	
	public Get(Interpretation x, Interpretation y) {
		i1 = x;
		i2 = y;
	}
	
	public short id() {
		return 17;
	}
	
	public String string() {
		return "get " + i1.string() + " from " + i2.string();
	}
}
