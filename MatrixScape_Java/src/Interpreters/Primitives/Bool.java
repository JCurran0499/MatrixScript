package Interpreters.Primitives;

import Interpreters.Primitive;

public class Bool extends Primitive {
    private final boolean bool;

    public Bool(boolean b) {
        bool = b;
    }

    /* Base Methods */

    public String id() {
        return "bool";
    }

    public String string() {
        return (bool ? "true" : "false");
    }
}
