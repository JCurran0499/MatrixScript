/*
 * John Curran
 * 
 * The Interpretation interface is used to create different types of Interpretation
 * objects. These objects serve as representations for the different possible 
 * MatrixScape commands. The parser interprets a String command and converts it
 * into the proper Interpretation object that is reflected in the command.
 * 
 * Interpretation objects serve the purpose of differentiating the different
 * MatrixScape commands in order for the simplifier to perform the proper
 * calculations.
 * 
 * Interpretation objects all have two methods in common (some may have additional
 * methods). The id() method returns a short number that is unique to the given
 * Interpretation class. This is used to determine the type of class that each
 * Interpretation object is. The string() method converts the object into String
 * form. This method is used by the MatrixScape class to print the object in
 * its proper form.
 */

package Interpreter;

public interface Interpretation {
	public short id();
	public String string();
}
