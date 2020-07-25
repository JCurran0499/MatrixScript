/*
 * John Curran
 * 
 * The "Identity" Interpretation represents the identity command. The 'form' field
 * denotes whether the request is for an identity matrix (not 0) or a zero vector (0)
 */

package Interpreter;

public class Identity implements Interpretation {
	public final Interpretation i;
	public final int form;
	
	public Identity(Interpretation x, int f) {
		i = x;
		form = f;
	}
	
	public short id() {
		return 27;
	}
	
	public String string() {
		if (form == 0)
			return "zero vector " + i.string();
		
		return "identity " + i.string();
	}
}
