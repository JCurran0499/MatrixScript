package Interpreters.Commands;

import Interpreters.Interpreter;
import Interpreters.Primitive;
import Interpreters.Variables.Variables;

public class Declare implements Interpreter {
    private final String varName;
    private final Interpreter expression;


    public Declare(String v, Interpreter e) {
        varName = v;
        expression = e;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p = expression.solve();
        if (p.id().equals("err"))
            return p;

        Declare.addVariable(varName, p);
        return p;
    }

    public String id() {
        return "declare";
    }

    /* Logic Methods */
    public static void addVariable(String varName, Primitive value) {
        Variables.variables().put(varName, value);
    }
}
