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
        return false;
    }
}
