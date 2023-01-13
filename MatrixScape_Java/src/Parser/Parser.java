package Parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.math.BigDecimal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Interpreters.Interpreter;
import Interpreters.Arithmetic.*;
import Interpreters.Commands.*;
import Interpreters.Comparisons.*;
import Interpreters.Primitives.*;

import Interpreters.Variables.Var;
import Matrix.Matrix;

public class Parser {
    public static Interpreter parse(String command) {
        // remove comment
        if (command.contains("//"))
            command = command.substring(0, command.indexOf("//"));

        if (command.isEmpty())
            return Null.returnNull();

        return parseTokens(tokenize(command));
    }

    private static ArrayList<Token> tokenize(String command) {
        command = command.strip().replaceAll("\\s+", " ");

        ArrayList<Token> tokenList = new ArrayList<>();
        Token token;
        boolean addedToken;

        while (command.length() > 0) {
            addedToken = false;

            // ---------- Parentheses ---------- \\
            if (command.startsWith("(")) {
                token = Token.parseBracket(command, '(', ')', TokenType.PAREN);
                tokenList.add(token);
                addedToken = true;

                command = command.substring(token.value().length() + 2).strip();
            }

            // ---------- Matrix ---------- \\
            else if (command.startsWith("[")) {
                token = Token.parseBracket(command, '[', ']', TokenType.MAT);
                tokenList.add(token);
                addedToken = true;

                command = command.substring(token.value().length()).strip();
            }

            // ---------- Subtraction & Negation ---------- \\
            else if (command.startsWith("-")) {
                TokenType prevType = tokenList.isEmpty() ? null : tokenList.get(tokenList.size() - 1).type();
                List<TokenType> subtractables =
                        Arrays.asList(TokenType.PAREN, TokenType.MAT, TokenType.NUM, TokenType.BOOL);
                if (command.substring(1, 2).matches("\\s") || subtractables.contains(prevType))
                    tokenList.add(new Token(TokenType.SUB, "-"));
                else
                    tokenList.add(new Token(TokenType.NEG, "-"));

                addedToken = true;
                command = command.substring(1).strip();
            }

            // ---------- Key Words & Symbols ---------- \\
            else {
                for (TokenType type : Token.equivalentSymbols.keySet()) {
                    Matcher m = Token.equivalentSymbols.get(type).matcher(command);
                    if (m.find() && m.start() == 0) {
                        token = new Token(type, m.group(1));
                        tokenList.add(token);
                        addedToken = true;

                        command = command.substring(m.end(1)).strip();
                        break;
                    }
                }
            }

            // ---------- Variable Names & Errors ---------- \\
            if (!addedToken) {
                Matcher m = Pattern.compile("[a-zA-Z_]\\w*").matcher(command);
                if (m.find() && m.start() == 0) {
                    tokenList.add(new Token(TokenType.VAR, m.group()));
                    command = command.substring(m.end()).strip();
                }

                // error text
                else {
                    StringBuilder errorText = new StringBuilder();
                    List<Character> stoppers = Arrays.asList(' ', '\t', '+', '-', '*', '/', '^', '!', ':',
                            '=', '<', '>', '~', ',', '(', '[');

                    while (!stoppers.contains(command.charAt(0))) {
                        errorText.append(command.charAt(0));
                        command = command.substring(1).strip();
                    }

                    tokenList.add(new Token(TokenType.ERR, errorText.toString()));
                }
            }
        }

        return tokenList;
    }

    private static Interpreter parseTokens(List<Token> tokens) {
        int index;

        // ---------- PRIMITIVES ---------- \\

        if (tokens.isEmpty())
            return Null.returnNull();

        if (tokens.size() == 1) {
            Token t = tokens.get(0);
            if (t.type() == TokenType.NUM)
                return new Num(new BigDecimal(t.value()));
            else if (t.type() == TokenType.BOOL)
                return new Bool(t.value().strip().equals("true"));
            else if (t.type() == TokenType.MAT)
                return new Mat(parseMatrixTokens(t.value()));
            else if (t.type() == TokenType.PAREN)
                return parseTokens(tokenize(t.value()));
            else if (t.type() == TokenType.VAR)
                return new Var(t.value());
        }

        // ---------- COMMANDS ---------- \\

        index = findFirstToken(tokens, TokenType.DECLARE);
        if (index > -1) {
            if (index != 1)
                return new Err("invalid variable declaration");

            return new Declare(tokens.get(0).value(), parseTokens(tokens.subList(2, tokens.size())));
        }

        // ---------- COMPARISONS ---------- \\

        index = findFirstToken(tokens, TokenType.EQUAL);
        if (index > -1)
            return new Equal(parseTokens(tokens.subList(0, index)), parseTokens(tokens.subList(index + 1, tokens.size())));

        index = findFirstToken(tokens, TokenType.GREAT);
        if (index > -1)
            return new Great(parseTokens(tokens.subList(0, index)), parseTokens(tokens.subList(index + 1, tokens.size())));

        index = findFirstToken(tokens, TokenType.LESS);
        if (index > -1)
            return new Less(parseTokens(tokens.subList(0, index)), parseTokens(tokens.subList(index + 1, tokens.size())));

        index = findFirstToken(tokens, TokenType.GTEQUAL);
        if (index > -1)
            return new GTEqual(parseTokens(tokens.subList(0, index)), parseTokens(tokens.subList(index + 1, tokens.size())));

        index = findFirstToken(tokens, TokenType.LTEQUAL);
        if (index > -1)
            return new LTEqual(parseTokens(tokens.subList(0, index)), parseTokens(tokens.subList(index + 1, tokens.size())));

        // ---------- ARITHMETIC ---------- \\

        index = findLastToken(tokens, TokenType.ADD);
        if (index > -1)
            return new Add(parseTokens(tokens.subList(0, index)), parseTokens(tokens.subList(index + 1, tokens.size())));

        index = findLastToken(tokens, TokenType.SUB);
        if (index > -1)
            return new Sub(parseTokens(tokens.subList(0, index)), parseTokens(tokens.subList(index + 1, tokens.size())));

        index = findLastToken(tokens, TokenType.MULT);
        if (index > -1)
            return new Mult(parseTokens(tokens.subList(0, index)), parseTokens(tokens.subList(index + 1, tokens.size())));

        index = findLastToken(tokens, TokenType.DIV);
        if (index > -1)
            return new Div(parseTokens(tokens.subList(0, index)), parseTokens(tokens.subList(index + 1, tokens.size())));

        index = findFirstToken(tokens, TokenType.MERGE);
        if (index > -1)
            return new Merge(parseTokens(tokens.subList(0, index)), parseTokens(tokens.subList(index + 1, tokens.size())));

        index = findFirstToken(tokens, TokenType.POW);
        if (index > -1)
            return new Pow(parseTokens(tokens.subList(0, index)), parseTokens(tokens.subList(index + 1, tokens.size())));

        if (tokens.get(tokens.size() - 1).type() == TokenType.FACT)
            return new Fact(parseTokens(tokens.subList(0, tokens.size() -1)));

        if (tokens.get(0).type() == TokenType.NEG)
            return new Neg(parseTokens(tokens.subList(1, tokens.size())));

        if (tokens.get(0).type() == TokenType.NOT)
            return new Not(parseTokens(tokens.subList(1, tokens.size())));

        // ---------- ERROR ---------- \\

        return new Err("command not recognized");
    }

    // -------------- HELPER METHODS -------------- \\

    private static Matrix parseMatrixTokens(String m) {
        List<Token> tokenList = tokenize(m);
        return null;
    }

    private static int findFirstToken(List<Token> tokens, TokenType type) {
        for (int i = 0; i < tokens.size(); i++)
            if (tokens.get(i).type() == type)
                return i;

        return -1;
    }

    private static int findLastToken(List<Token> tokens, TokenType type) {
        for (int i = tokens.size() - 1; i >= 0; i--)
            if (tokens.get(i).type() == type)
                return i;

        return -1;
    }
}
