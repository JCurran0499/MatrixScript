/*
 * John Curran
 * 
 * The "Mat" Interpretation represents a matrix
 */

package Interpreter;

import Matrix.Matrix;

public class Mat implements Interpretation{
	public final Matrix i;
	
	public Mat(Matrix m) {
		i = m;
	}
	
	public short id() {
		return 0;
	}
	
	public String string() {
		return i.toString();
	}
	
	public Mat negate() {
		return new Mat(i.multiply(-1));
	}
}
