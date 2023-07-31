package App.Parser.Interpreters.Primitives;

import App.Parser.Interpreters.Primitive;

public class Null extends Primitive {
    private static final Null nullReturn = new Null();

    private Null() {}

    public String id() {
        return "null";
    }

    public String string() {
        return "";
    }

    public boolean equals(Primitive p) {
        return p == nullReturn;
    }

    public static Null returnNull() {
        return nullReturn;
    }
}
