package app.parser.interpreters.primitives;

import app.parser.interpreters.Primitive;

public class Err extends Primitive {
    private final String message;

    public Err(String err) {
        message = err;
    }

    /* Base Methods */

    public String id() {
        return "err";
    }

    public String string() {
        return "Error: " + message;
    }

    public boolean equals(Primitive p) {
        if (!id().equals(p.id()))
            return false;

        return message.equals(Err.cast(p).message);
    }

    public static Err cast(Primitive p) {
        if (!p.id().equals("err"))
            throw new ClassCastException("incompatible primitive cast");

        return (Err) p;
    }
}
