package Interpreters.Commands;

import Interpreters.Interpreter;
import Interpreters.Primitive;
import Interpreters.Primitives.Null;
import Interpreters.Variables.Variables;

public class Set implements Interpreter {
    private final String varName;
    private final Interpreter expression;


    public Set(String v, Interpreter e) {
        varName = v;
        expression = e;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p = expression.solve();
        if (p.id().equals("err"))
            return p;

        Set.addVariable(varName, p);
        return Null.returnNull();
    }

    public String id() {
        return "set";
    }

    public String string() {
        return "";
    }

    public boolean equals(Primitive p) {
        return false;
    }

    /* Logic Methods */
    public static void addVariable(String varName, Primitive value) {
        Variables.variables().put(varName, value);
    }
}
