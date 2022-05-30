/*
 * John Curran
 * 
 * The "Rank" Interpretation represents the rank of a matrix
 */

package Interpreter;

public class Rank implements Interpretation {
	public final Interpretation i;
	
	public Rank(Interpretation x) {
		i = x;
	}
	
	public short id() {
		return 21;
	}
	
	public String string() {
		return "rank " + i.string();
	}
}
