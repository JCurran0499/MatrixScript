package Interpreters.Primitives;

import Interpreters.Interpreter;
import Interpreters.Primitive;
import Interpreters.Variables.Variables;

public class Set extends Primitive {
    private final String varName;
    private final Primitive expression;


    public Set(String v, Interpreter e) {
        varName = v;
        expression = e.solve();
    }

    /* Base Methods */

    public Primitive solve() {
        if (expression.id().equals("err"))
            return expression;

        return this;
    }

    public String id() {
        return "set";
    }

    public String string() {
        return varName + " = " + expression.string();
    }

    /* Logic Methods */
    public void addVariable() {
        Variables.variables().put(varName, expression);
    }
}
