package Interpreters.Commands;

import Interpreters.Interpreter;
import Interpreters.Primitive;
import Interpreters.Primitives.Err;
import Interpreters.Variables.Variables;

public class Var implements Interpreter {
    private final String name;

    public Var(String n) {
        name = n;
    }

    /* Base Methods */

    public Primitive solve() {
        if (Variables.variables().containsKey(name)) return Variables.variables().get(name);
        else return new Err("no variable \"" + name + "\"");
    }

    public String id() {
        return "var";
    }
}
