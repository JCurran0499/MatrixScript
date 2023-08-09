package app.parser.primitives;

import app.parser.Primitive;
import app.parser.PrimitiveID;

public class Null extends Primitive {
    private static final Null nullReturn = new Null();

    private Null() {}

    public String id() {
        return PrimitiveID.NULL.name;
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

    public static boolean is(Primitive p) {
        return p.id().equals(PrimitiveID.NULL.name);
    }
}
