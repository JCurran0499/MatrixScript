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

    public boolean equals(Primitive p) {
        if (!id().equals(p.id()))
            return false;

        return bool == ((Bool) p).bool;
    }

    /* Logic Methods */

    public Bool not() {
        return new Bool(!bool);
    }
}
