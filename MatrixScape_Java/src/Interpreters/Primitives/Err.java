package Interpreters.Primitives;

import Interpreters.Primitive;

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
}
