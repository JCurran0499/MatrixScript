package Interpreters.Variables;

import Interpreters.Interpreter;
import Interpreters.Primitive;
import Interpreters.Primitives.Err;

public class Var implements Interpreter {
    private final String name;

    public Var(String n) {
        name = n;
    }

    /* Base Methods */

    public Primitive solve() {
        if (!SessionHandler.varExists(name))
            return new Err("variable '" + name + "' does not exist");

        return SessionHandler.getVar(name);
    }

    public String id() {
        return "var";
    }
}
