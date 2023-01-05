package Interpreters.Primitives;

import Interpreters.Primitive;

public class Null extends Primitive {
    private static final Null nullReturn = new Null();

    private Null() {}

    public String id() {
        return "null";
    }

    public String string() {
        return null;
    }

    public boolean equals(Primitive p) {
        return p == nullReturn;
    }

    public static Null returnNull() {
        return nullReturn;
    }
}
