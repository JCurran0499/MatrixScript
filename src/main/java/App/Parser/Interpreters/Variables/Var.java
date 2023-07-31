package App.Parser.Interpreters.Variables;

import App.Parser.Interpreters.Interpreter;
import App.Parser.Interpreters.Primitive;
import App.Parser.Interpreters.Primitives.Err;

public class Var implements Interpreter {

    private final String token;
    private final String name;

    public Var(String t, String n) {
        token = t;
        name = n;
    }

    /* Base Methods */

    public Primitive solve() {
        if (!SessionHandler.varExists(token, name))
            return new Err("variable '" + name + "' does not exist");

        return SessionHandler.getVar(token, name);
    }

    public String id() {
        return "var";
    }
}
