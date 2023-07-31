package App.Parser.Interpreters.Commands;

import App.Parser.Interpreters.Interpreter;
import App.Parser.Interpreters.Primitive;
import App.Parser.Interpreters.Primitives.Err;
import App.Parser.Interpreters.Variables.SessionHandler;
import App.Parser.Token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Declare implements Interpreter {

    private final String token;
    private final String varName;
    private final Interpreter expression;


    public Declare(String t, String v, Interpreter e) {
        token = t;
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

        addVariable(varName, p);
        return p;
    }

    public String id() {
        return "declare";
    }

    /* Logic Methods */
    public void addVariable(String varName, Primitive value) {
        SessionHandler.setVar(token, varName, value);
    }

    private static boolean isValidVariable(String name) {
        Matcher m;
        for (Pattern p : Token.equivalentSymbols.values())
            if (p.matcher(name).matches())
                return false;

        Pattern validVariable = Pattern.compile("[a-zA-Z_]\\w*");
        Matcher matcher = validVariable.matcher(name);
        return matcher.matches();
    }
}
