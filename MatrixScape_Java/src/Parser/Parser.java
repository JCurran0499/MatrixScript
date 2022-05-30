/*
 * John Curran
 * 
 * The parser takes in a one-line String and returns an Interpretation object
 * reflecting the command within the String. The parser is designed to work
 * in an order that obeys the order of operations rules inherent in mathematics
 * and the MatrixScape semantics.
 * 
 * The parser is not designed to perform many calculations, instead only putting
 * the String into Interpretation form. The simplifier performs the necessary
 * calculations on the Interpretation object before passing it onto the
 * MatrixScape class.
 */

package Parser;

import java.util.Map;

import Matrix.Matrix;

import Interpreter.Interpretation;
import Interpreter.Var;
import Interpreter.Compare;
import Interpreter.Add;
import Interpreter.Sub;
import Interpreter.Div;
import Interpreter.Mat;
import Interpreter.Num;
import Interpreter.Mult;
import Interpreter.Pow;
import Interpreter.Declare;
import Interpreter.Bool;
import Interpreter.Rows;
import Interpreter.Cols;
import Interpreter.Size;
import Interpreter.Square;
import Interpreter.Symmetrical;
import Interpreter.Set;
import Interpreter.Get;
import Interpreter.Transpose;
import Interpreter.Rref;
import Interpreter.Det;
import Interpreter.Rank;
import Interpreter.Invertible;
import Interpreter.Inverse;
import Interpreter.Tuple;
import Interpreter.Range;
import Interpreter.Err;
import Interpreter.Identity;
import Interpreter.Type;
import Interpreter.List;
import Interpreter.Logic;
import Interpreter.Factorial;
import Interpreter.Null;

public class Parser {
	public static Interpretation parse(String s, Map<String, Interpretation> vars) {
		if (s == null || vars == null)
			return new Null();
		
		//this is done so commands that involve parentheses do not confuse
		//the parser if there is no whitespace between the command and
		//the parentheses
		s = s.replaceAll("\\(", " \\(").replaceAll("\\)", "\\) ").trim();
		
		String[] arr;
			
		if (s.equals(""))
			return new Null();
		
		//these arrays keep track of which parts of the String are within
		//parentheses and matrix blocks
		int[] parens = indexes(s, '(', ')');
		int[] blocks = indexes(s, '[', ']');
		
		//checks for uneven parentheses or matrix blocks
		if (containsNegatives(parens) || parens[parens.length - 1] != 0)
			return new Err("unbalanced parentheses");
		if (containsNegatives(blocks) || blocks[blocks.length - 1] != 0)
			return new Err("incomplete matrix");
		
		if (s.startsWith("type ")) {
			s = s.substring(5);
			return new Type(parse(s, vars));
		}
		
		if (s.equals("list"))
			return new List();
		
		if (isNum(s))
			return new Num(Double.parseDouble(s));
		
		//declaring a variable has the lowest priority in the order of operations and is thus checked first
		int index = indexOf(s, "=", parens, blocks, s.length() - 1, -1, vars);
		if (index >= 0 && (index == 0 || (s.charAt(index - 1) != '<' && s.charAt(index - 1) != '>' 
						&& s.charAt(index - 1) != '!' && s.charAt(index - 1) != '='))) {
			
			arr = split(s, "=", parens, blocks, -1, vars, 2);			
			arr[0] = arr[0].trim();
			if (arr[0].equals(""))
				return new Err("invalid declaration");
			
			return new Declare(arr[0], parse(arr[1], vars));
		}

		//logical statements "and" and "or" have the next lower priority, equal to each other
		String priority = firstPriority(s, new String[] {" and ", " or "}, parens, blocks, s.length() - 1, -1, vars);
		if (priority != null) {
			arr = split(s, priority, parens, blocks, -1, vars, 2);
			return new Logic(priority.trim(), parse(arr[0], vars), parse(arr[1], vars));
		}
		
		if (s.startsWith("not ")) {
			s = s.substring(4);
			return new Logic("not", parse(s, vars), new Null());
		}
		
		//check for logical operators
		priority = firstPriority(s, new String[] {"==", "!=", ">=", "<=", ">", "<"}, parens, blocks, s.length() - 1, -1, vars);
		if (priority != null) {
			if (priority.equals("==")) {
				arr = split(s, "==", parens, blocks, -1, vars, 2);
				return new Compare("==", parse(arr[0], vars), parse(arr[1], vars));
			}
			
			if (priority.equals("!=")) {
				arr = split(s, "!=", parens, blocks, -1, vars, 2);
				return new Compare("!=", parse(arr[0], vars), parse(arr[1], vars));
			}
			
			if (priority.equals(">=")) {
				arr = split(s, ">=", parens, blocks, -1, vars, 2);
				return new Compare(">=", parse(arr[0], vars), parse(arr[1], vars));
			}
			
			if (priority.equals("<=")) {
				arr = split(s, "<=", parens, blocks, -1, vars, 2);
				return new Compare("<=", parse(arr[0], vars), parse(arr[1], vars));
			}
			
			if (priority.equals(">")) {
				arr = split(s, ">", parens, blocks, -1, vars, 2);
				return new Compare(">", parse(arr[0], vars), parse(arr[1], vars));
			}

			arr = split(s, "<", parens, blocks, -1, vars, 2);
			return new Compare("<", parse(arr[0], vars), parse(arr[1], vars));
		}
		
		/* the following are MatrixScape specific commands that involve tuples
		  (and are thus of lower priority than tuples) */
		
		if (s.startsWith("get ")) {
			s = s.substring(4);
			parens = indexes(s, '(', ')');
			blocks = indexes(s, '[', ']');
			
			index = indexOf(s, " from ", parens, blocks, 0, 1, vars);
			if (index == -1)
				return new Err("invalid request");
			
			arr = split(s, " from ", parens, blocks, 1, vars, 2);
			return new Get(parse(arr[0], vars), parse(arr[1], vars));
		}
		
		if (s.startsWith("size ")) {
			s = s.substring(5);
			return new Size(parse(s, vars));
		}
		
		//check for tuples
		index = indexOf(s, ",", parens, blocks, 0, 1, vars);
		if (index >= 0) {
			arr = split(s, ",", parens, blocks, 1, vars, 2);
			return new Tuple(parse(arr[0], vars), parse(arr[1], vars));
		}
		
		/* the following are MatrixScape specific commands that do not involve
		  tuples (and are thus of higher priority than tuples) */
		
		if (s.startsWith("set ")) {
			s = s.substring(4);
			parens = indexes(s, '(', ')');
			blocks = indexes(s, '[', ']');
			
			index = indexOf(s, " from ", parens, blocks, 0, 1, vars);
			if (index == -1)
				return new Err("invalid set");
			
			arr = split(s, " from ", parens, blocks, 1, vars, 2);
			
			parens = indexes(arr[1], '(', ')');
			blocks = indexes(arr[1], '[', ']');
			
			index = indexOf(arr[1], " to ", parens, blocks, 0, 1, vars);
			if (index == -1)
				return new Err("invalid set");
			
			String[] arr1 = split(arr[1], " to ", parens, blocks, 1, vars, 2);
			arr = new String[] {arr[0], arr1[0], arr1[1]};
			return new Set(parse(arr[0], vars), parse(arr[1], vars), parse(arr[2], vars));
		}
		
		if (s.startsWith("rows ") || s.startsWith("row ")) {
			s = s.substring(4);
			return new Rows(parse(s, vars));
		}
		
		if (s.startsWith("cols ") || s.startsWith("col ")) {
			s = s.substring(4);
			return new Cols(parse(s, vars));
		}
		
		if (s.startsWith("square? ")) {
			s = s.substring(8);
			return new Square(parse(s, vars));
		}
		
		if (s.startsWith("symmetrical? ")) {
			s = s.substring(13);
			return new Symmetrical(parse(s, vars));
		}
		
		if (s.startsWith("invertible? ")) {
			s = s.substring(12);
			return new Invertible(parse(s, vars));
		}
		
		if (s.startsWith("inverse ")) {
			s = s.substring(8);
			return new Inverse(parse(s, vars));
		}
		
		if (s.startsWith("transpose ")) {
			s = s.substring(10);
			return new Transpose(parse(s, vars));
		}
		
		if (s.startsWith("rref ")) {
			s = s.substring(5);
			return new Rref(parse(s, vars));
		}
		
		if (s.startsWith("det ")) {
			s = s.substring(4);
			return new Det(parse(s, vars));
		}
		
		if (s.startsWith("rank ")) {
			s = s.substring(5);
			return new Rank(parse(s, vars));
		}
		
		if (s.startsWith("identity ")) {
			s = s.substring(9);
			return new Identity(parse(s, vars), 1);
		}
		
		if (s.startsWith("zero vector ")) {
			s = s.substring(12);
			return new Identity(parse(s, vars), 0);
		}
		
		index = indexOf(s, ":", parens, blocks, 0, 1, vars);
		if (index >= 0) {
			arr = split(s, ":", parens, blocks, 1, vars, 2);
			return new Range(parse(arr[0], vars), parse(arr[1], vars));
		}
		
		//check for addition or subtraction, the highest priority in mathematical expressions
		priority = firstPriority(s, new String[] {"+", "-"}, parens, blocks, s.length() - 1, -1, vars);
		if (priority != null) {
			if (priority.equals("+")) {
				arr = split(s, "+", parens, blocks, -1, vars, 2);
				return new Add(parse(arr[0], vars), parse(arr[1], vars));
			}
			
			arr = split(s, "-", parens, blocks, -1, vars, 2);
			return new Sub(parse(arr[0], vars), parse(arr[1], vars));
		}
		
		//check for multiplication or division
		priority = firstPriority(s, new String[] {"*", "/"}, parens, blocks, s.length() - 1, -1, vars);
		if (priority != null) {
			if (priority.equals("*")) {
				arr = split(s, "*", parens, blocks, -1, vars, 2);
				return new Mult(parse(arr[0], vars), parse(arr[1], vars));
			}
			
			arr = split(s, "/", parens, blocks,-1, vars, 2);
			return new Div(parse(arr[0], vars), parse(arr[1], vars));
		}
		
		//negative numbers
		if (s.startsWith("-")) {
			s = s.substring(1);
			Interpretation i1 = Simplifier.simplify(parse(s, vars), vars);
			
			if (i1.id() == -1)
				return i1;
			
			if (i1.id() == 0)
				return ((Mat) i1).negate();
			if (i1.id() == 1)
				return ((Num) i1).negate();
			if (i1.id() == 25)
				return ((Tuple) i1).negate();
			if (i1.id() == 26)
				return ((Range) i1).negate();
			
			return new Err("invalid negation");
		}
		
		//exponents
		index = indexOf(s, "^", parens, blocks, 0, 1, vars);
		if (index >= 0) {
			arr = split(s, "^", parens, blocks, 1, vars, 2);
			return new Pow(parse(arr[0], vars), parse(arr[1], vars));
		}
		
		//check for factorials
		if (s.endsWith("!")) {
			s = s.substring(0, s.length() - 1);
			return new Factorial(parse(s, vars));
		}
		
		
		//check for booleans
		if (s.equals("true"))
			return new Bool(true);
		
		if (s.equals("false"))
			return new Bool(false);
		
		
		//part (or all) of a command in parentheses
		if (s.startsWith("(") && s.endsWith(")"))
			return parse(s.substring(1, s.length() - 1), vars);
		
		//check for a matrix
		if (s.startsWith("[") && s.endsWith("]")) {
			s = parseMatrix(s.substring(1, s.length() - 1), vars);
			
			//parseMatrix() returns a String that begins with "error: "
			//if there is an error within the brackets
			if (s.startsWith("error: "))
				return new Err(s.substring(7));
			
			try {
				Matrix m = new Matrix(s);
				return new Mat(m);
			} catch (ArrayIndexOutOfBoundsException e) { return new Err("invalid matrix"); }
		}
		
		//check for a variable name
		if (!s.contains(" ")) {
			return new Var(s);
		}
		
		return new Err("invalid command");
	}
	
	/* the following are helper methods used only in this class to assist in the parsing process */	
	
	//this method takes in a String from within the matrix blocks in the command,
	//and returns a simplified String that is suitable for a Matrix class constructor
	private static String parseMatrix(String s, Map<String, Interpretation> vars) {
		if (s.matches(" *"))
			return "error: empty matrix";
		
		//this is done so commands or negations that involve parentheses
		//(example: -(5), row(m)) are not separated because of whitespace
		s = s.replaceAll(" \\(", "\\(");
		
		int[] parens = indexes(s, '(', ')');
		int[] blocks = indexes(s, '[', ']');
		
		String m = "";
		String[] arr1 = split(s, ";", parens, blocks, 1, vars, -1);

		//first split the String up into different rows of the matrix, and then split
		//each row up by whitespace and work with each value
		for (int i = 0; i < arr1.length; i++) {
			s = arr1[i].trim();
			String[] arr2 = split(s, " ", indexes(s, '(', ')'), indexes(s, '[', ']'), 1, vars, -1);
			
			for (int j = 0; j < arr2.length; j++) {
				if (!arr2[j].matches(" ")) {
					Interpretation val = Simplifier.simplify(parse(arr2[j], vars), vars);
					
					if (val.id() == -1)
						return "error: " + ((Err) val).err;
					if (val.id() != 1 && val.id() != 26)
						return "error: must use integers";
					
					if (val.id() == 1)
						m = m + Double.toString(((Num) val).i.doubleValue()) + " ";
					else {					
						int f = ((Num) (((Range) val).i1)).i.intValue();
						int b = ((Num) (((Range) val).i2)).i.intValue();
						
						if (f < b) 
							for (int x = f; x <= b; x++)
								m += Integer.toString(x) + " ";
						else
							for (int x = f; x >= b; x--)
								m += Integer.toString(x) + " ";
					}
				}
			}
			
			if (i < arr1.length - 1)
				m = m + "; ";
		}

		return m;
	}
	
	
	//returns whether the String is a number
	private static boolean isNum(String s) {
		if (s.startsWith("+"))
			return false;
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) { return false; }
	}
	
	//returns an array of integers that tracks the scope of parentheses or blocks
	private static int[] indexes(String s, char front, char back) {
		int[] parenStatus = new int[s.length()];
		int paren = 0;
		for (int i = 0; i < s.length(); i++) {
			
			if (s.charAt(i) == front)
				paren++;
			else if (s.charAt(i) == back)
				paren--;
			
			parenStatus[i] = paren;
		}
		
		return parenStatus;
	}
	
	//returns whether an array has negative values
	private static boolean containsNegatives(int[] arr) {
		for (int j : arr)
			if (j < 0)
				return true;
		
		return false;
	}
	
	//similar to the indexOf(char) String method, but this method searches in a given direction and ignores
	//values within the scope of parentheses or blocks
	private static int indexOf(String s, String c, int[] parens, int[] blocks, int start, int direction, Map<String, Interpretation> vars) {
		int index;
		if (direction > 0) {
			index = s.indexOf(c, start);
			start = index + c.length();
		}
		else {
			index = s.lastIndexOf(c, start);
			start = index - c.length();
		}
		
		if (index < 0)
			return -1;
		if (parens[index] == 0 && blocks[index] == 0) {
			
			//the dash could either mean subtraction or a negative number, and this is determined
			//by checking if the value to the left of the dash is a number or not
			if (c.equals("-")) {
				Interpretation i = Simplifier.simplify(parse(s.substring(0, index), vars), vars);
				if (i.id() == 0 || i.id() == 1)
					return index;
			
			} else return index;
		}
		
		return indexOf(s, c, parens, blocks, start, direction, vars);
	}
	
	//similar to the split(String) String method, but this method searches in a given direction and ignores
	//values within the scope of parentheses or blocks
	private static String[] split(String s, String c, int[] parens, int[] blocks, int direction, Map<String, Interpretation> vars, int limit) {
		int start;
		if (direction > 0)
			start = 0;
		else
			start = s.length() - 1;
		
		int index = indexOf(s, c, parens, blocks, start, direction, vars);
		if (index == -1)
			return new String[] {s};
		
		String first = s.substring(0, index).trim();
		String second = s.substring(index + c.length(), s.length()).trim();
		
		if (limit > 2 || limit < 0) {
			
			int[] parens1 = indexes(first, '(', ')');
			int[] parens2 = indexes(second, '(', ')');
			int[] blocks1 = indexes(first, '[', ']');
			int[] blocks2 = indexes(second, '[', ']');
			
			if (direction > 0) {
				if (indexOf(second, c, parens2, blocks2, start, direction, vars) == -1)
					return new String[] {first, second};
				else {
					String[] split = split(second, c, parens2, blocks2, direction, vars, limit - 1);
					return concat(new String[] {first}, split);
				}
			} else {
				start = first.length() - 1;
				if (indexOf(first, c, parens1, blocks1, start, direction, vars) == -1)
					return new String[] {first, second};
				else {
					String[] split = split(first, c, parens1, blocks1, direction, vars, limit - 1);
					return concat(split, new String[] {second});
				}
			}
		} else return new String[] {first, second};
	}
	
	//returns the first occurence of any String in the array, searching in the given direction
	//and ignoring values within the scope of parentheses or blocks
	private static String firstPriority(String s, String[] arr, int[] parens, int[] blocks, int start, int direction, Map<String, Interpretation> vars) {
		String priority = arr[0];
		int index = indexOf(s, priority, parens, blocks, start, direction, vars);
		
		for (String tok : arr) {
			int index2 = indexOf(s,tok,parens, blocks, start,direction, vars);
			
			if (index2 > index) {
				priority = tok;
				index = indexOf(s, priority, parens, blocks, start, direction, vars);
			}
		}
		
		if (index == -1)
			return null;
		
		return priority;
	}
	
	//concatenates two arrays
	private static String[] concat(String[] x, String[] y) {
		String[] arr = new String[x.length + y.length];
		for (int i = 0; i < x.length; i++)
			arr[i] = x[i];
		for (int i = 0; i < y.length; i++)
			arr[i + x.length] = y[i];
		
		return arr;
	}
}