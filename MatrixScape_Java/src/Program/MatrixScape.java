/*
 * John Curran
 * 
 * MatrixScape is an interpreted command language used for interacting with 
 * linear algebra problems and calculations. There are five data types in MatrixScape;
 * the Matrix, Number, Boolean, Tuple, and Range. The user can set variables
 * to any of these data types, or they can interact with them by writing them out
 * directly. 
 * 
 * Matrix: the mathematical concept of a matrix
 * Number: any number within the confines of the computer's memory
 * Boolean: the programming concept of a boolean
 * Tuple: a set of values that cannot be altered
 * Range: the representation of the range of numbers between and including two integers
 * 
 * MatrixScape allows the user to create matrices, numbers, booleans, tuples, and ranges,
 * and use them to calculate various linear algebra problems. This includes but is not
 * limited to adding and subtracting matrices and numbers, reducing matrices, and augmenting
 * multiple matrices together. The user writes each command at a time, and the computer
 * executes the command with each new line.
 * 
 * MatrixScape is a continuously evolving command language, and new additions to the 
 * capabilities of the language will be added over time.
 */

package Program;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import Parser.Parser;
import Parser.Simplifier;

import Interpreter.Interpretation;
import Interpreter.Tuple;
import Interpreter.Range;
import Interpreter.Err;
import Interpreter.Num;
import Interpreter.Mat;
import Interpreter.Bool;
import Interpreter.Declare;
import Interpreter.Set;
import Interpreter.Cols;
import Interpreter.Var;
import Interpreter.Rows;

public class MatrixScape {
	
	//maps each variable name to its value
	private static TreeMap<String, Interpretation> variables = new TreeMap<String, Interpretation>();
	
	public static void main(String args[]) {
		System.out.println("Welcome to MatrixScape!\n");
		
		boolean exit = true;
		boolean complete = true;
		Scanner scanner = new Scanner(System.in);
		Interpretation i1;
		Interpretation i2;
		Interpretation i3;
		String s;
		
		//the "exit" keyword will end the program
		while (exit) {
			System.out.print(">> ");
			String entry = scanner.nextLine().trim();
			
			//any text after "//" will be considered a comment and ignored by the computer
			int index = entry.indexOf("//");
			if (index >= 0)
				entry = entry.substring(0, index).trim();

			//if a command that sets a variable or alters a matrix ends with ";",
			//it will not print anything after be carried out
			complete = true;
			if (entry.endsWith(";")) {
				complete = false;
				entry = entry.substring(0, entry.length() - 1);
			}
			
			if (entry.equals("exit"))
				exit = false;
			else {
				//parse and simplify the command, and then execute it as follows
				i1 = Simplifier.simplify(Parser.parse(entry, variables), variables);					
				switch (i1.id()) {
				case -1:
					err(((Err) i1).err);
					break;
				
				case 0:
					(((Mat) i1).i).print();
					break;
					
				case 1:
					print(((Num) i1).i.setScale(5,RoundingMode.HALF_UP).doubleValue());
					System.out.println();
					break;
					
				case 8:
					s = ((Declare) i1).name;
					i2 = ((Declare) i1).i;
					variables.put(s, i2);
					
					if (i2.id() == 0 && complete) {
						System.out.println(s + " = ");
						((Mat) i2).i.print();
					} else if (complete)
						System.out.println(s + " = " + ((Declare) i1).i.string() + "\n");
					break;					
					
				case 10:
					System.out.println(((Bool) i1).i + "\n");
					break;
					
				case 16:
					i2 = ((Set) i1).i2;
					i3 = ((Set) i1).i3;
					i1 = ((Set) i1).i1;
					
					String print = "";
					if (i2.id() == 9)
						print = ((Var) i2).name + " = \n";
					
					i2 = Simplifier.simplify(i2, variables);
					if (i2.id() == -1)
						err(((Err) i2).err);
					else if (i2.id() != 0)
						err("must use matrix");
					else {
						
						if (i1.id() == 11 && i3.id() == 0) {
							
							i1 = Simplifier.simplify(((Rows) i1).i, variables);
							if (i1.id() == -1)
								err(((Err) i1).err);
							else if (isInteger(i1)) {
								
								try {
								(((Mat) i2).i).setRow(((Num) i1).i.intValue(), ((Mat) i3).i);
								if (complete) {
									System.out.print(print);
									((Mat) i2).i.print();
								}
								} catch (ArrayIndexOutOfBoundsException e) { err("invalid dimensions"); }
							} else err("must use integer");
						}
						else if (i1.id() == 12 && i3.id() == 0) {
							
							i1 = Simplifier.simplify(((Cols) i1).i, variables);
							if (i1.id() == -1)
								err(((Err) i1).err);
							else if (isInteger(i1)) {
								
								try {
								(((Mat) i2).i).setColumn(((Num) i1).i.intValue(), ((Mat) i3).i);
								if (complete) {
									System.out.print(print);
									((Mat) i2).i.print();
								}
								} catch (ArrayIndexOutOfBoundsException e) { err("invalid dimensions"); }
							} else err("must use integer");
						} 
						else if (i1.id() == 26 && i3.id() == 1) {
							
							i1 = Simplifier.simplify(i1, variables);
							if (i1.id() == -1)
								err(((Err) i1).err);
							else {
								int r1 = ((Num) ((Range) i1).i1).i.intValue();
								int r2 = ((Num) ((Range) i1).i2).i.intValue();
								
								try {
								(((Mat) i2).i).setValue(r1, r2, ((Num) i3).i.doubleValue());
								if (complete) {
									System.out.print(print);
									((Mat) i2).i.print();
								}
								} catch (ArrayIndexOutOfBoundsException e) { err("invalid dimensions"); }
							} 
						} else err("invalid set");
					}
					break;
					
				case 24:
					break;
					
				case 25:
					System.out.println(((Tuple) i1).string() + "\n");					
					break;
					
				case 26:
					System.out.println(((Range) i1).string() + "\n");
					break;
					
				case 28:
					System.out.println(i1.string() + "\n");
					break;
					
				case 29:
					ArrayList<String> matrices = new ArrayList<String>();
					ArrayList<String> numbers = new ArrayList<String>();
					ArrayList<String> bools = new ArrayList<String>();
					ArrayList<String> tuples = new ArrayList<String>();
					ArrayList<String> ranges = new ArrayList<String>();
					
					for(String n : variables.keySet()) {
						i1 = variables.get(n);
						if (i1.id() == 0)
							matrices.add(n);
						else if (i1.id() == 1)
							numbers.add(n);
						else if (i1.id() == 10)
							bools.add(n);
						else if (i1.id() == 25)
							tuples.add(n);
						else if (i1.id() == 26)
							ranges.add(n);
					}
					
					System.out.print("matrices:  ");
					for (int i = 0; i < matrices.size() - 1; i++)
						System.out.print(matrices.get(i) + ", ");
					if (matrices.size() > 0) System.out.print(matrices.get(matrices.size() - 1));
					System.out.println();
					
					System.out.print("numbers:   ");
					for (int i = 0; i < numbers.size() - 1; i++)
						System.out.print(numbers.get(i) + ", ");
					if (numbers.size() > 0) System.out.print(numbers.get(numbers.size() - 1));
					System.out.println();
					
					System.out.print("booleans:  ");
					for (int i = 0; i < bools.size() - 1; i++)
						System.out.print(bools.get(i) + ", ");
					if (bools.size() > 0) System.out.print(bools.get(bools.size() - 1));
					System.out.println();
					
					System.out.print("tuples:    ");
					for (int i = 0; i < tuples.size() - 1; i++)
						System.out.print(tuples.get(i) + ", ");
					if (tuples.size() > 0) System.out.print(tuples.get(tuples.size() - 1));
					System.out.println();
					
					System.out.print("ranges:    ");
					for (int i = 0; i < ranges.size() - 1; i++)
						System.out.print(ranges.get(i) + ", ");
					if (ranges.size() > 0) System.out.print(ranges.get(ranges.size() - 1));
					System.out.println("\n");
					break;
					
				default:
					err("invalid command");
					break;
				}
			}
		}
		scanner.close();
	}
	
	
	private static void err(String errMessage) {
		System.out.println("Error: " + errMessage + "\n");
	}
	
	//print a value, dropping the decimal end if it is an integer
	private static void print(double d) {
		if (d % 1 == 0)
			System.out.println((int) d);
		else
			System.out.println(d);
	}
	
	private static boolean isInteger(Interpretation i) {
		return i.id() == 1 && ((Num) i).i.doubleValue() % 1 == 0;
	}
}