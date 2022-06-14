package Parser;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Interpreters.Interpreter;
import Interpreters.Variables.Variables;
import Interpreters.Primitives.*;
import Interpreters.Arithmetic.*;
import Matrix.Matrix;

public class Parser {
    public static Interpreter parse(String command) {
        int index;
        String[] inputs;

        command = command.strip();
        if (command.isEmpty())
            return Null.returnNull();

        // Comments
        index = command.indexOf("//");
        if (index >= 0)
            command = command.substring(0, index).strip();

        // Parentheses management
        if (command.matches("\\(.*\\)"))
            return parse(command.substring(1, command.length() - 1));

        int[] parensArray = parensArray(command);
        if (parensArray == null || parensArray[parensArray.length - 1] != 0)
            return new Err("unbalanced parentheses");

        // Variable assignment
        inputs = patternMatch(Pattern.compile("(.+)=([^=].*)"), command,
                new String[] {"=[^=]"}, parensArray);
        if (inputs != null) {
            String varName = inputs[0].strip();
            if (!isValidVariable(varName))
                return new Err("invalid variable name");

            return new Set(varName, parse(inputs[1]));
        }

        // ------ Basic Primitives ------ //

        // Number
        if (command.matches("\\d+\\.?\\d*"))
            return new Num(new BigDecimal(command));

        // Boolean
        if (command.matches("true|false"))
            return new Bool(command.equals("true"));

        // Matrix
        if (command.matches("\\[.*]")) {
            command = command.substring(1, command.length() - 1);
            try {
                Matrix m = new Matrix(command);
                return new Mat(m);
            } catch (ArrayIndexOutOfBoundsException e) { return new Err("invalid matrix"); }
        }

        // ------ Basic Arithmetic ------ //
        /* This is done in reverse PEMDAS order, since commands discovered
        *  first are compiled last in this recursive program. */

        // Addition
        inputs = patternMatch(Pattern.compile("(.+)\\+(.+)"), command,
                new String[] {"\\+"}, parensArray);
        if (inputs != null) {
            return new Add(parse(inputs[0]), parse(inputs[1]));
        }

        // Subtraction
        inputs = patternMatch(Pattern.compile("(.+)-(.+)"), command,
                new String[] {"-"}, parensArray);
        if (inputs != null) {
            return new Sub(parse(inputs[0]), parse(inputs[1]));
        }

        // Multiplication
        inputs = patternMatch(Pattern.compile("(.+)\\*(.+)"), command,
                new String[] {"\\*"}, parensArray);
        if (inputs != null) {
            return new Mult(parse(inputs[0]), parse(inputs[1]));
        }

        // Division
        inputs = patternMatch(Pattern.compile("(.+)/(.+)"), command,
                new String[] {"/"}, parensArray);
        if (inputs != null) {
            return new Add(parse(inputs[0]), parse(inputs[1]));
        }

        // ------ Advanced Primitives ------ //

        // Tuple
        inputs = patternMatch(Pattern.compile("(.+),(.+)"), command,
                new String[] {","}, parensArray);
        if (inputs != null) {
            return new Tuple(parse(inputs[0]), parse(inputs[1]));
        }

        // Range
        inputs = patternMatch(Pattern.compile("(.+):(.+)"), command,
                new String[] {":"}, parensArray);
        if (inputs != null) {
            return new Range(parse(inputs[0]), parse(inputs[1]));
        }

        // ------ Advanced Arithmetic ------ //

        // Negation
        if (command.matches("-.+"))
            return new Neg(parse(command.substring(1)));

        // Exponents
        inputs = patternMatch(Pattern.compile("(.+)\\^(.+)"), command,
                new String[] {"\\^"}, parensArray);
        if (inputs != null) {
            return new Pow(parse(inputs[0]), parse(inputs[1]));
        }



        // Variable call
        if (Variables.variables().containsKey(command))
            return Variables.variables().get(command);

        return new Err("invalid command");
    }

    private static String[] patternMatch(Pattern pattern, String command, String[] separators, int[] parensArray) {
        Matcher match = pattern.matcher(command);
        if (!match.matches())
            return null;

        // check that the command is not within parentheses
        for (String s : separators) {
            Pattern p = Pattern.compile(s);
            Matcher m = p.matcher(command);

            m.find();
            int index = m.start();
            if (parensArray[index] != 0)
                return null;
        }

        String[] responses = new String[match.groupCount()];
        for (int i = 0; i < match.groupCount(); i++)
            responses[i] = match.group(i + 1);
        return responses;
    }

    private static boolean isValidVariable(String name) {
        Pattern validVariable = Pattern.compile("[a-zA-Z]+\\w*");
        Matcher matcher = validVariable.matcher(name);
        return matcher.matches();
    }

    private static int[] parensArray(String command) {
        int level = 0;
        int i = 0;
        int[] parensArray = new int[command.length()];
        for (char c : command.toCharArray()) {
            if (c == '(')
                level++;
            else if (c == ')')
                level--;

            if (level < 0)
                return null;

            parensArray[i] = level;
            i++;
        }

        return parensArray;
    }
}
