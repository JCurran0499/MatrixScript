/*
 * John Curran
 * 
 * The simplifier performs most of the MatrixScape computation. It takes in an
 * Interpretation object, and simplifies it to one of several Interpretation 
 * objects that the MatrixScape class then uses. All mathematical and logical
 * computation in MatrixScape is done in this class.
 * 
 * The simplifier performs a different action for every type of Interpretation,
 * and then returns one of just a few Interpretation types, depending on what
 * the original argument was.
 */

package Parser;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;

import Matrix.Matrix;

import Interpreter.Add;
import Interpreter.Bool;
import Interpreter.Cols;
import Interpreter.Det;
import Interpreter.Div;
import Interpreter.Compare;
import Interpreter.Err;
import Interpreter.Get;
import Interpreter.Interpretation;
import Interpreter.Inverse;
import Interpreter.Invertible;
import Interpreter.Mat;
import Interpreter.Mult;
import Interpreter.Num;
import Interpreter.Pow;
import Interpreter.Range;
import Interpreter.Rank;
import Interpreter.Rows;
import Interpreter.Rref;
import Interpreter.Declare;
import Interpreter.Size;
import Interpreter.Square;
import Interpreter.Sub;
import Interpreter.Symmetrical;
import Interpreter.Transpose;
import Interpreter.Tuple;
import Interpreter.Set;
import Interpreter.Var;
import Interpreter.Identity;
import Interpreter.Type;
import Interpreter.Logic;
import Interpreter.Factorial;

public class Simplifier {
	public static Interpretation simplify(Interpretation x, Map<String, Interpretation> vars) {
		
		Interpretation i1;
		Interpretation i2;
		Interpretation i3;
	
		switch(x.id()) {
		
		//error
		case -1:
			return x;
		
		//matrix
		case 0:
			//check for invalid matrix
			if (((Mat) x).i == null)
				return new Err("invalid matrix");
			
			return x;
			
		//number
		case 1:
			return x;
			
		//addition
		case 2:
			i1 = simplify(((Add) x).i1, vars);
			i2 = simplify(((Add) x).i2, vars);
			
			if (i1.id() == -1)
				return i1;
			if (i2.id() == -1)
				return i2;
			
			//two matrices or two numbers can be added together
			if (i1.id() == 0 && i2.id() == 0)
				return simplify(new Mat((((Mat) i1).i).add(((Mat) i2).i)), vars);
			else if (i1.id() == 1 && i2.id() == 1)
				return new Num((((Num) i1).i).add(((Num) i2).i));
			else 
				return new Err("invalid addition");
		
		//subtraction
		case 3:
			i1 = simplify(((Sub) x).i1, vars);
			i2 = simplify(((Sub) x).i2, vars);
			
			if (i1.id() == -1)
				return i1;
			if (i2.id() == -1)
				return i2;
			
			if (i1.id() == 0 && i2.id() == 0)
				return simplify(new Mat((((Mat) i1).i).subtract(((Mat) i2).i)), vars);
			else if (i1.id() == 1 && i2.id() == 1)
				return new Num((((Num) i1).i).subtract(((Num) i2).i));
			else 
				return new Err("invalid subtraction");
		
		//multiplication
		case 4:
			i1 = simplify(((Mult) x).i1, vars);
			i2 = simplify(((Mult) x).i2, vars);
			
			if (i1.id() == -1)
				return i1;
			if (i2.id() == -1)
				return i2;
			
			//two matrices, two numbers, or a matrix and a number can be multiplied together
			if (i1.id() == 0 && i2.id() == 0)
				return simplify(new Mat((((Mat) i1).i).multiply(((Mat) i2).i)), vars);
			else if (i1.id() == 1 && i2.id() == 1)
				return new Num((((Num) i1).i).multiply(((Num) i2).i));
			else if (i1.id() == 0 && i2.id() == 1)
				return simplify(new Mat((((Mat) i1).i).multiply(((Num) i2).i.doubleValue())), vars);
			else if (i1.id() == 1 && i2.id() == 0)
				return simplify(new Mat((((Mat) i2).i).multiply(((Num) i1).i.doubleValue())), vars);
			else
				return new Err("invalid multiplication");
		
		//division
		case 5:
			i1 = simplify(((Div) x).i1, vars);
			i2 = simplify(((Div) x).i2, vars);
			
			if (i1.id() == -1)
				return i1;
			if (i2.id() == -1)
				return i2;
			
			if (i2.id() == 1 && ((Num) i2).i.doubleValue() == 0)
				return new Err("cannot divide by zero");
			
			//two numbers or a matrix and a number can be divided
			if (i1.id() == 1 && i2.id() == 1)
				return new Num((((Num) i1).i).divide(((Num) i2).i, MathContext.DECIMAL128));
			else if (i1.id() == 0 && i2.id() == 1)
				return simplify(new Mat((((Mat) i1).i).divide(((Num) i2).i.doubleValue())), vars);
			else 
				return new Err("invalid division");
		
		//exponents
		case 6:
			i1 = simplify(((Pow) x).i1, vars);
			i2 = simplify(((Pow) x).i2, vars);
			
			if (i1.id() == -1)
				return i1;
			if (i2.id() == -1)
				return i2;
			
			if (i1.id() == 1 && i2.id() == 1) 
				return new Num(new BigDecimal(Math.pow(((Num) i1).i.doubleValue(),((Num) i2).i.doubleValue()), MathContext.DECIMAL128));
			else if (i1.id() == 0 && isInteger(i2) && ((Num) i2).i.doubleValue() >= 0)
				return simplify(new Mat((((Mat) i1).i).toPower(((Num) i2).i.intValue())), vars);
			else 
				return new Err("invalid exponent");
		
		//comparison operators
		case 7:
			i1 = simplify(((Compare) x).i1, vars);
			i2 = simplify(((Compare) x).i2, vars);
			
			if (i1.id() == -1)
				return i1;
			if (i2.id() == -1)
				return i2;
			
			if (i1.id() != i2.id())
				return new Err("mismatching data types");
			
			switch(((Compare) x).operator) {
			
			//simplify all possible logical operators to a boolean value
			case "==":
				if (i1.id() == 1 && i2.id() == 1)
					return new Bool(((Num) i1).i.equals(((Num) i2).i));
				else if (i1.id() == 0 && i2.id() == 0)
					return new Bool((((Mat) i1).i).equals(((Mat) i2).i));
				else if (i1.id() == 10 && i2.id() == 10)
					return new Bool((((Bool) i1).i) == (((Bool) i2).i));
				else if (i1.id() == 25 && i2.id() == 25)
					return new Bool(((Tuple) i1).equals((Tuple) i2));
				else if (i1.id() == 26 && i2.id() == 26) 
					return new Bool(((Range) i1).equals((Range) i2));
				else 
					return new Err("invalid data types");
				
			case ">":
				if (i1.id() == 1 && i2.id() == 1)
					return new Bool(((Num) i1).i.compareTo(((Num) i2).i) > 0);
				else if (i1.id() == 26 && i2.id() == 26)
					return new Bool(((Range) i1).size() > ((Range) i2).size());
				else 
					return new Err("invalid data types");
				
			case "<":
				if (i1.id() == 1 && i2.id() == 1)
					return new Bool(((Num) i1).i.compareTo(((Num) i2).i) < 0);
				else if (i1.id() == 26 && i2.id() == 26)
					return new Bool(((Range) i1).size() < ((Range) i2).size());
				else 
					return new Err("invalid data types");
				
			case ">=":
				if (i1.id() == 1 && i2.id() == 1)
					return new Bool(((Num) i1).i.compareTo(((Num) i2).i) >= 0);
				else if (i1.id() == 26 && i2.id() == 26)
					return new Bool(((Range) i1).size() >= ((Range) i2).size());
				else 
					return new Err("invalid data types");
				
			case "<=":
				if (i1.id() == 1 && i2.id() == 1)
					return new Bool(((Num) i1).i.compareTo(((Num) i2).i) <= 0);
				else if (i1.id() == 26 && i2.id() == 26)
					return new Bool(((Range) i1).size() <= ((Range) i2).size());
				else 
					return new Err("invalid data types");
				
			case "!=":
				if (i1.id() == 1 && i2.id() == 1)
					return new Bool(!((Num) i1).i.equals(((Num) i2).i));
				else if (i1.id() == 0 && i2.id() == 0)
					return new Bool(!(((Mat) i1).i).equals(((Mat) i2).i));
				else if (i1.id() == 10 && i2.id() == 10)
					return new Bool((((Bool) i1).i) != (((Bool) i2).i));
				else if (i1.id() == 25 && i2.id() == 25)
					return new Bool(!((Tuple) i1).equals((Tuple) i2));
				else if (i1.id() == 26 && i2.id() == 26) 
					return new Bool(!((Range) i1).equals((Range) i2));
				else 
					return new Err("invalid data types");
				
			default:
				return new Err("invalid command");
			}
		
		//variable declaration
		case 8:
			//check if the variable name is valid
			if (!validName(((Declare) x).name))
				return new Err("invalid variable name");
			
			i1 = simplify(((Declare) x).i, vars);
			
			if (i1.id() == 24)
				return new Err("invalid declaration");
			
			if (i1.id() == -1)
				return i1;
			
			//check if the variable type is valid
			if (!validVariable(i1))
				return new Err("invalid variable type");
			
			return new Declare(((Declare) x).name, i1);
		
		//variable
		case 9:
			String n = ((Var) x).name;
			
			//verify that a variable with the given name exists
			if (vars.containsKey(n))
				return vars.get(n);
			else
				return new Err("variable \"" + n + "\" does not exist");
		
		//boolean
		case 10:
			return x;
		
		//matrix row request
		case 11:
			i1 = simplify(((Rows) x).i, vars);
			
			if (i1.id() == -1)
				return i1;
			
			if (i1.id() == 0)
				return new Num(((Mat) i1).i.rows());
			else
				return new Err("must use matrix");
		
		//matrix column request
		case 12:
			i1 = simplify(((Cols) x).i, vars);
			
			if (i1.id() == -1)
				return i1;
			
			if (i1.id() == 0)
				return new Num(((Mat) i1).i.cols());
			else
				return new Err("must use matrix");
		
		//size
		case 13:
			i1 = simplify(((Size) x).i, vars);
			
			if (i1.id() == -1)
				return i1;
			
			if (i1.id() == 0)
				return new Num(((Mat) i1).i.size());
			if (i1.id() == 25)
				return new Num(((Tuple) i1).size());
			else
				return new Err("must use matrix or tuple");
		
		//square
		case 14:
			i1 = simplify(((Square) x).i, vars);
			
			if (i1.id() == -1)
				return i1;
			
			//return whether the matrix is square
			if (i1.id() == 0)
				return new Bool(((Mat) i1).i.isSquare());
			else
				return new Err("must use matrix");
			
		//symmetrical
		case 15:
			i1 = simplify(((Symmetrical) x).i, vars);
			
			if (i1.id() == -1)
				return i1;
			
			if (i1.id() == 0)
				return new Bool(((Mat) i1).i.isSymmetrical());
			else
				return new Err("must use matrix");
			
		//set command
		case 16:
			i1 = ((Set) x).i1;
			i2 = ((Set) x).i2;
			i3 = simplify(((Set) x).i3, vars);
			
			if (i1.id() == -1)
				return i1;
			if (i2.id() == -1)
				return i2;
			if (i3.id() == -1)
				return i3;
			
			return new Set(i1, i2, i3);
		
		//get command
		case 17:
			i1 = ((Get) x).i1;
			i2 = simplify(((Get) x).i2, vars);
			
			if (i1.id() == -1)
				return i1;
			if (i2.id() == -1)
				return i2;
			
			//most of the get command is carried out in this class, unlike the set command
			
			//the get command can retrieve rows of a matrix, columns of a matrix,
			//values in a matrix, and values in a tuple
			if (i1.id() == 11 && i2.id() == 0) {
				i1 = simplify(((Rows) i1).i, vars);
				if (i1.id() == -1)
					return i1;
				
				if (isInteger(i1) && i2.id() == 0) {
					Mat m = new Mat(((Mat) i2).i.getRow(((Num) i1).i.intValue()));
					if (m.i == null)
						return new Err("out of bounds");
					return m;
				}
				return new Err("must use integer");
			}
			if (i1.id() == 12 && i2.id() == 0) {
				i1 = simplify(((Cols) i1).i, vars);
				if (i1.id() == -1)
					return i1;
				
				if (isInteger(i1)) {
					Mat m = new Mat(((Mat) i2).i.getColumn(((Num) i1).i.intValue()));
					if (m.i == null)
						return new Err("out of bounds");
					return m;
				}
				return new Err("must use integer");
			}
			i1 = simplify(i1, vars);
			if (i1.id() == 26 && i2.id() == 0) {
				if (i1.id() == -1)
					return i1;
				
				i3 = i2;
				i2 = simplify(((Range) i1).i2, vars);
				i1 = simplify(((Range) i1).i1, vars);
				
				try {
				return new Num(((Mat) i3).i.getValue(((Num) i1).i.intValue(), ((Num) i2).i.intValue()));
				} catch (ArrayIndexOutOfBoundsException e) { return new Err("out of bounds"); }
			}
			if (isInteger(i1) && i2.id() == 25)
				return ((Tuple) i2).get(((Num) i1).i.intValue());
			
			return new Err("invalid request");
		
		//transpose of a matrix
		case 18:
			i1 = simplify(((Transpose) x).i, vars);
			
			if (i1.id() == -1)
				return i1;
			
			if (i1.id() == 0)
				return simplify(new Mat(((Mat) i1).i.transpose()), vars);
			else
				return new Err("must use matrix");
		
		//reduced row echelon form of a matrix
		case 19:
			i1 = simplify(((Rref) x).i, vars);
			
			if (i1.id() == -1)
				return i1;
			
			if (i1.id() == 0)
				return simplify(new Mat(((Mat) i1).i.rref()), vars);
			else
				return new Err("must use matrix");
		
		//determinant of a matrix
		case 20:
			i1 = simplify(((Det) x).i, vars);
			
			if (i1.id() == -1)
				return i1;
			
			if (i1.id() == 0)
				return new Num(((Mat) i1).i.determinant());
			else
				return new Err("must use matrix");
		
		//rank of a matrix
		case 21:
			i1 = simplify(((Rank) x).i, vars);
			
			if (i1.id() == -1)
				return i1;
			
			if (i1.id() == 0)
				return new Num(((Mat) i1).i.rank());
			else
				return new Err("must use matrix");
		
		//whether a matrix is invertible
		case 22:
			i1 = simplify(((Invertible) x).i, vars);
			
			if (i1.id() == -1)
				return i1;
			
			if (i1.id() == 0)
				return new Bool(((Mat) i1).i.invertible());
			else
				return new Err("must use matrix");
			
		//inverse of a matrix
		case 23:
			i1 = simplify(((Inverse) x).i, vars);
			
			if (i1.id() == -1)
				return i1;
			
			if (i1.id() == 0)
				return simplify(new Mat(((Mat) i1).i.inverse()), vars);
			else
				return new Err("must use matrix");
		
		//null (empty) value
		case 24:
			return x;
		
		//tuple
		case 25:
			i1 = simplify(((Tuple) x).i1, vars);
			i2 = simplify(((Tuple) x).i2, vars);
			
			if (i1.id() == -1)
				return i1;
			if (i2.id() == -1)
				return i2;
			
			//tuples can only contain the 5 data types
			if (!validVariable(i1) || !validVariable(i2))
				return new Err("invalid tuple element");
			
			return new Tuple(i1, i2);
		
		//range
		case 26:
			i1 = simplify(((Range) x).i1, vars);
			i2 = simplify(((Range) x).i2, vars);
			
			if (i1.id() == -1)
				return i1;
			if (i2.id() == -1)
				return i2;
			
			//range must consist of integers only
			if (isInteger(i1) && isInteger(i2))
				return new Range(i1, i2);
			else if (i1.id() == 0 && i2.id() == 0)
				return simplify(new Mat(((Mat) i1).i.augment(((Mat) i2).i)), vars);
			else return new Err("range must consist of integers");
			
		//identity matrix command
		case 27:
			i1 = simplify(((Identity) x).i, vars);
			int form = ((Identity) x).form;
			
			if (i1.id() == -1)
				return i1;
			
			//the identity Interpretation object works with both identity matrices and zero vectors
			if (isInteger(i1)) {
				if (form == 0)
					return simplify(new Mat(Matrix.ZeroVector(((Num) i1).i.intValue())), vars);
				else return simplify(new Mat(Matrix.Identity(((Num) i1).i.intValue())), vars);
			}
			else return new Err("must use integer");
			
		//type command
		case 28:
			i1 = simplify(((Type) x).i, vars);
			if (i1.id() == -1)
				return i1;
			
			return new Type(i1);
			
		//list command
		case 29:
			return x;
			
		// and/or/not logic
		case 30:
			i1 = simplify(((Logic) x).i1, vars);
			i2 = simplify(((Logic) x).i2, vars);
			
			if (i1.id() == -1)
				return i1;
			if (i2.id() == -1)
				return i2;
			
			if (i1.id() != 10)
				return new Err("invalid boolean");
			
			switch(((Logic) x).operator) {
			
			case "not":
				return new Bool(!(((Bool) i1).i));
			case "and":
				if (i2.id() != 10)
					return new Err("invalid boolean");
				
				return new Bool(((Bool) i1).i && ((Bool) i2).i);
			case "or":
				if (i2.id() != 10)
					return new Err("invalid boolean");
				
				return new Bool(((Bool) i1).i || ((Bool) i2).i);
			}
			
		//factorial
		case 31:
			i1 = simplify(((Factorial) x).i, vars);
			
			if (i1.id() == -1)
				return i1;
			
			if (isInteger(i1) && ((Num) i1).i.intValue() >= 0) {
				int fact = ((Num) i1).i.intValue();
				if (fact == 0)
					fact = 1;
				
				for (int i = fact - 1; i > 0; i--)
					fact *= i;
				
				return new Num(fact);
			}
			else return new Err("must use positive non-negative integer");
			
		default:
			return new Err("invalid command");
		}
	}
	
	/* the following are helper methods used only in this class to assist in the simplifying process */		
	
	//returns whether the Interpretation is a type that can be stored as a variable (matrix, number, boolean, tuple, range)
	private static boolean validVariable(Interpretation i) {
		return (i.id() == 0 || i.id() == 1 || i.id() == 10 || i.id() == 25 || i.id() == 26);
	}
	
	//returns whether the Interpretation is an integer
	private static boolean isInteger(Interpretation i) {
		return i.id() == 1 && ((Num) i).i.doubleValue() % 1 == 0;
	}
	
	//returns whether the String can be used as a valid MatrixScape variable name
	private static boolean validName(String s) {
		//list of MatrixScape keywords that variables cannot use as names
		String[] invalidNames = new String[] {"set", "get", "rows", "row", "cols", "col", "size", "inverse",
							"transpose", "rref", "det", "rank", "true", "false", "exit", "and", "or",
							"not", "to", "from", "type", "list", "identity"};
		
		/* MatrixScape variable names can consist of letters, numbers, and underscores.
		   They must contain at least one letter and cannot be a MatrixScape keyword */
		if (!s.matches("([a-zA-Z0-9]|_)*([a-zA-Z]+)([a-zA-Z0-9]|_)*"))
			return false;
		
		for (String n : invalidNames)
			if (s.equals(n))
				return false;
		
		return true;
	}
}