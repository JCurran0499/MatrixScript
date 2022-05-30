/*
 * John Curran
 * 
 * The "Div" Interpretation represents the division of two Interpretation values
 */

package Interpreter;

public class Div implements Interpretation{
	public final Interpretation i1;
	public final Interpretation i2;
	
	public Div(Interpretation x, Interpretation y) {
		i1 = x;
		i2 = y;
	}
	
	public short id() {
		return 5;
	}
	
	public String string() {
		return i1.string() + " / " + i2.string();
	}
}
