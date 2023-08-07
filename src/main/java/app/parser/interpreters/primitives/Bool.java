package app.parser.interpreters.primitives;

import app.parser.interpreters.Primitive;

public class Bool extends Primitive {

    private static final Bool trueBool = new Bool(true);
    private static final Bool falseBool = new Bool(false);

    private final boolean bool;

    private Bool(boolean b) {
        bool = b;
    }

    public static Bool of(boolean b) {
        return b
            ? trueBool
            : falseBool;
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

        return bool == Bool.cast(p).bool;
    }

    /* Logic Methods */

    public boolean bool() {
        return bool;
    }

    public Bool not() {
        return Bool.of(!bool);
    }

    public static Bool cast(Primitive p) {
        if (!p.id().equals("bool"))
            throw new ClassCastException("incompatible primitive cast");

        return (Bool) p;
    }
}
