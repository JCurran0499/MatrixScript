package Interpreters.Commands;

import Interpreters.Interpreter;
import Interpreters.Primitive;
import Interpreters.Primitives.Err;
import Interpreters.Variables.VarHandler;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Declare implements Interpreter {
    private static final List<String> invalidTerms =
            Arrays.asList("true", "false", "get", "set", "from", "to", "rref", "det", "row", "col", "size",
                    "identity", "zerovector", "transpose", "append", "inverse", "rank");

    private final String varName;
    private final Interpreter expression;


    public Declare(String v, Interpreter e) {
        varName = v;
        expression = e;
    }

    /* Base Methods */

    public Primitive solve() {
        if (!isValidVariable(varName))
            return new Err("invalid variable name");

        Primitive p = expression.solve();

        if (p.id().equals("err"))
            return p;
        if (p.id().equals("null"))
            return new Err("variable '" + varName + "' must be set to a value");

        p.printValue = false;

        Declare.addVariable(varName, p);
        return p;
    }

    public String id() {
        return "declare";
    }

    /* Logic Methods */
    public static void addVariable(String varName, Primitive value) {
        VarHandler.setVar(varName, value);
    }

    private static boolean isValidVariable(String name) {
        if (invalidTerms.contains(name))
            return false;

        Pattern validVariable = Pattern.compile("[a-zA-Z_]\\w*");
        Matcher matcher = validVariable.matcher(name);
        return matcher.matches();
    }
}
