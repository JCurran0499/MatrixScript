/*
 * John Curran
 * 
 * The "Range" Interpretation represents a range of numbers. These numbers must 
 * be integers. Ranges are used to represent positions in a matrix and, more 
 * importantly, to include a full range of numbers in a row when 
 * creating a matrix.
 * 
 * In addition to numbers, a range of two matrices augments them, appending
 * the second matrix into the end of the first matrix.
 */

package Interpreter;

public class Range implements Interpretation {
	public final Interpretation i1;
	public final Interpretation i2;
	
	public Range(Interpretation x, Interpretation y) {
		i1 = x;
		i2 = y;
	}
	
	public short id() {
		return 26;
	}
	
	public boolean equals(Range r) {
		if (i1.id() != 1 || i2.id() != 1 || r.i1.id() != 1 || r.i2.id() != 1)
			return false;
		
		return (((Num) i1).i.equals(((Num) r.i1).i) && ((Num) i2).i.equals(((Num) r.i2).i));
	}
	
	public int size() {
		if (i1.id() != 1 || i2.id() != 1)
			return -1;
		
		return (((Num) i1).i.subtract(((Num) i2).i)).abs().intValue();
	}
	
	public String string() {
		return i1.string() + ":" + i2.string();
	}
	
	public Range negate() {
		return new Range(i2, i1);
	}
}
