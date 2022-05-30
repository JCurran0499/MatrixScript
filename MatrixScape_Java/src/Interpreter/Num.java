/*
 * John Curran
 * 
 * The "Num" Interpretation represents a number. The field for the data value is
 * a BigDecimal object in order to ensure precision when executing mathematical
 * computations.
 */

package Interpreter;

import java.math.BigDecimal;

public class Num implements Interpretation {
	
	public final BigDecimal i;
	
	public Num(BigDecimal d) {
		i = d;
	}
	
	public Num(double d) {
		i = new BigDecimal(d);
	}
	
	public short id() {
		return 1;
	}
	
	public String string() {
		String n;
		Double d = i.doubleValue();
		if (d % 1 == 0)
			n = Integer.toString(d.intValue());
		else
			n = Double.toString(d);
		
		return n;
	}
	
	public Num negate() {
		return new Num(i.negate());
	}
}
