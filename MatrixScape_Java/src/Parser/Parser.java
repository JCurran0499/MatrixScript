package Parser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Interpreters.Interpreter;
import Interpreters.Primitives.*;

public class Parser {

    private static final List<String> invalidTerms =
            Arrays.asList("true", "false", "get", "set", "from", "to", "rref", "det", "row", "col", "size",
                    "identity", "zerovector", "transpose", "append", "inverse", "rank");


    public static Interpreter parse(String command) {
        // remove comment
        if (command.contains("//"))
            command = command.substring(0, command.indexOf("//"));

        command = command.strip().replaceAll("\\s+", " ");
        if (command.isEmpty())
            return Null.returnNull();

        return parseTokens(tokenize(command));
    }

    public static ArrayList<Token> tokenize(String command) {
        ArrayList<Token> tokenList = new ArrayList<>();
        Token token;

        while (command.length() > 0) {
            // ---------- Parentheses ---------- \\
            if (command.startsWith("(")) {
                token = Token.parseBracket(command, '(', ')', TokenType.PAREN);
                tokenList.add(token);

                command = command.substring(token.value().length());
            }

            // ---------- Matrix ---------- \\
            else if (command.startsWith("[")) {
                token = Token.parseBracket(command, '[', ']', TokenType.MAT);
                tokenList.add(token);

                command = command.substring(token.value().length());
            }

            // Move past spaces
            else if (command.startsWith(" "))
                command = command.substring(1);

            // ---------- Key Words & Symbols ---------- \\
            else {
                for (TokenType type : Token.equivalentSymbols.keySet()) {
                    Matcher m = Token.equivalentSymbols.get(type).matcher(command);
                    if (m.find() && m.start() == 0) {
                        token = new Token(type, m.group(1));
                        tokenList.add(token);

                        command = command.substring(m.end(1));
                        break;
                    }
                }
            }
        }

        return tokenList;
    }

    private static Interpreter parseTokens(ArrayList<Token> tokens) {
        return null;
    }

    private static boolean isValidVariable(String name) {
        if (invalidTerms.contains(name))
            return false;

        Pattern validVariable = Pattern.compile("[a-zA-Z_]\\w*");
        Matcher matcher = validVariable.matcher(name);
        return matcher.matches();
    }


}
