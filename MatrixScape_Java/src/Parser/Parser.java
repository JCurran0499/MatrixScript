package Parser;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Interpreters.Interpreter;
import Interpreters.Commands.Set;
import Interpreters.Variables.Variables;
import Interpreters.Primitives.*;
import Interpreters.Arithmetic.*;

public class Parser {

    private static final List<String> invalidTerms =
            Arrays.asList("true", "false", "get", "set", "from", "to", "rref", "det", "row", "col", "size",
                    "identity", "zerovector", "transpose", "append", "inverse", "rank");


    public static Interpreter parse(String command) {
        String[] inputs;

        command = command.strip().replaceAll("\\s+", " ");
        if (command.isEmpty())
            return Null.returnNull();

        // Parentheses management
        if (command.matches("\\(.*\\)"))
            return parse(command.substring(1, command.length() - 1));

        int[] parensArray = bracketArray(command, '(', ')');
        int[] blocksArray = bracketArray(command, '[', ']');
        if (parensArray == null || parensArray[parensArray.length - 1] != 0)
            return new Err("unbalanced parentheses");
        if (blocksArray == null || blocksArray[blocksArray.length - 1] != 0)
            return new Err("unbalanced matrix brackets");

        // Variable assignment
        inputs = patternMatch(Pattern.compile("(.+)=([^=].*)"), command,
                new String[] {"=[^=]"}, new int[][] {parensArray, blocksArray});
        if (inputs != null) {
            String varName = inputs[0].strip();
            if (!isValidVariable(varName))
                return new Err("invalid variable name");

            return new Set(varName, parse(inputs[1]));
        }

        // ------ Basic Primitives ------ //

        // Number
        if (command.matches("\\d*\\.?\\d+"))
            return new Num(new BigDecimal(command));

        // Boolean
        if (command.matches("true|false"))
            return new Bool(command.equals("true"));

        // Matrix
        if (command.matches("\\[.*]")) {
            command = command.substring(1, command.length() - 1).strip();
            List<List<String>> matrixList = new ArrayList<>();
            List<String> splits = new ArrayList<>();

            // split into rows
            int splitter = command.indexOf(";");
            while(splitter >= 0) {
                if (parensArray[splitter] == 0 && blocksArray[splitter] == 0) {
                    matrixList.add(new ArrayList<>());
                    splits.add(command.substring(0, splitter));
                    command = command.substring(splitter + 1, command.length() - 1);
                }

                splitter = command.indexOf(";", splitter + 1);
            }

            // split the rows into elements
            for (int i = 0; i < matrixList.size(); i++) {
                String row = splits.get(i);
                int[] rowParens = bracketArray(row, '(', ')');
                int[] rowBrackets = bracketArray(row, '[', ']');

                splitter = row.indexOf(" ");
                while(splitter >= 0) {
                    if (rowParens[splitter] == 0 && rowBrackets[splitter] == 0) {
                        matrixList.add(new ArrayList<>());
                        splits.add(command.substring(0, splitter));
                        command = command.substring(splitter + 1, command.length() - 1);
                    }

                    splitter = command.indexOf(";", splitter + 1);
                }
            }
        }

        // ------ Basic Arithmetic ------ //
        /* This is done in reverse PEMDAS order, since commands discovered
        *  first are compiled last in this recursive program. */

        // Addition
        inputs = patternMatch(Pattern.compile("(.+)\\+(.+)"), command,
                new String[] {"\\+"}, new int[][] {parensArray, blocksArray});
        if (inputs != null) {
            return new Add(parse(inputs[0]), parse(inputs[1]));
        }

        // Subtraction
        inputs = patternMatch(Pattern.compile("(.+)-(.+)"), command,
                new String[] {"-"}, new int[][] {parensArray, blocksArray});
        if (inputs != null) {
            return new Sub(parse(inputs[0]), parse(inputs[1]));
        }

        // Multiplication
        inputs = patternMatch(Pattern.compile("(.+)\\*(.+)"), command,
                new String[] {"\\*"}, new int[][] {parensArray, blocksArray});
        if (inputs != null) {
            return new Mult(parse(inputs[0]), parse(inputs[1]));
        }

        // Division
        inputs = patternMatch(Pattern.compile("(.+)/(.+)"), command,
                new String[] {"/"}, new int[][] {parensArray, blocksArray});
        if (inputs != null) {
            return new Div(parse(inputs[0]), parse(inputs[1]));
        }

        // ------ Advanced Primitives ------ //

        // Tuple
        inputs = patternMatch(Pattern.compile("(.+),(.+)"), command,
                new String[] {","}, new int[][] {parensArray, blocksArray});
        if (inputs != null) {
            return new Tuple(parse(inputs[0]), parse(inputs[1]));
        }

        // Range
        inputs = patternMatch(Pattern.compile("(.+):(.+)"), command,
                new String[] {":"}, new int[][] {parensArray, blocksArray});
        if (inputs != null) {
            return new Merge(parse(inputs[0]), parse(inputs[1]));
        }

        // ------ Commands ------ //



        // ------ Advanced Arithmetic ------ //

        // Negation
        if (command.matches("-.+"))
            return new Neg(parse(command.substring(1)));

        // Exponents
        inputs = patternMatch(Pattern.compile("(.+)\\^(.+)"), command,
                new String[] {"\\^"}, new int[][] {parensArray, blocksArray});
        if (inputs != null) {
            return new Pow(parse(inputs[0]), parse(inputs[1]));
        }

        // Factorial
        if (command.matches(".+!"))
            return new Fact(parse(command.substring(0, command.length() - 1)));



        // Variable call
        if (Variables.variables().containsKey(command))
            return Variables.variables().get(command);

        return new Err("invalid command");
    }

    private static String[] patternMatch(String pattern, String command, int[] parenArrays, int[][] blockArrays) {
        String[] separators = pattern.split("~");
    }

    private static boolean isValidVariable(String name) {
        if (invalidTerms.contains(name))
            return false;

        Pattern validVariable = Pattern.compile("[a-zA-Z_]\\w*");
        Matcher matcher = validVariable.matcher(name);
        return matcher.matches();
    }

    private static int[] bracketArray(String command, char opener, char closer) {
        int level = 0;
        int i = 0;
        int[] blockArray = new int[command.length()];
        for (char c : command.toCharArray()) {
            if (c == opener)
                level++;
            else if (c == closer)
                level--;

            if (level < 0)
                return null;

            blockArray[i] = level;
            i++;
        }

        return blockArray;
    }
}
