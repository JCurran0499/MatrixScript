/*
 * John Curran
 * 
 * The "Det" Interpretation represents the determinant of a matrx
 */

package Interpreter;

public class Det implements Interpretation {
	public final Interpretation i;
	
	public Det(Interpretation x) {
		i = x;
	}
	
	public short id() {
		return 20;
	}
	
	public String string() {
		return "det " + i.string();
	}
}
