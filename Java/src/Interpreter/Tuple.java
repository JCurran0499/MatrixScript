/*
 * John Curran
 * 
 * The "Tuple" Interpretation represents a tuple object
 */

package Interpreter;

public class Tuple implements Interpretation {
	//a tuple with more than two objects will be represented
	//by a tuple of tuples, with one or both of these fields being
	//tuple objects
	public final Interpretation i1;
	public final Interpretation i2;
	
	public Tuple(Interpretation x, Interpretation y) {
		i1 = x;
		i2 = y;
	}
	
	public short id() {
		return 25;
	}
	
	public boolean equals(Tuple t) {		
		return equalHelper(i1, t.i1) && equalHelper(i2, t.i2);
	}
	
	//get a value from within the tuple
	public Interpretation get(int index) {
		if (index < 0 || index >= size())
			return new Err("out of bounds");
		
		if (index < size(i1)) {
			if (i1.id() != 25)
				return i1;
			
			return ((Tuple) i1).get(index);
		}
		else {
			index -= size(i1);
			if (i2.id() != 25)
				return i2;
			
			return ((Tuple) i2).get(index);
		}
	}
	
	//total number of values in the tuple
	public int size() {
		return size(this);
	}
	
	public String string() {
		return i1.string() + ", " + i2.string();
	}
	
	public Tuple negate() {
		Interpretation t1 = i1;
		Interpretation t2 = i2;
		if (t1.id() == 25) {
			t1 = ((Tuple) t1).negate();
		}
		if (t2.id() == 25) {
			t2 = ((Tuple) t2).negate();
		}
		
		return new Tuple(t2, t1);
		
	}
	
	/* the following are private helper methods, used only in this class */
	
	private static boolean equalHelper(Interpretation i1, Interpretation i2) {
		if (i1.id() != i2.id())
			return false;
		
		if (i1.id() == 0)
			return ((Mat) i1).i.equals(((Mat) i2).i);
		if (i1.id() == 1)
			return ((Num) i1).i.doubleValue() == ((Num) i2).i.doubleValue();
		if (i1.id() == 10)
			return ((Bool) i1).i == ((Bool) i2).i;
		if (i1.id() == 25)
			return equalHelper(((Tuple) i1).i1, ((Tuple) i2).i1) && equalHelper(((Tuple) i1).i2, ((Tuple) i2).i2);
		if (i1.id() == 26)
			return ((Range) i1).equals((Range) i2);
		
		return false;
	}
	
	private static int size(Interpretation i) {
		if (i.id() != 25)
			return 1;
		
		Interpretation left = ((Tuple) i).i1;
		Interpretation right = ((Tuple) i).i2;
		return size(left) + size(right);
	}
}