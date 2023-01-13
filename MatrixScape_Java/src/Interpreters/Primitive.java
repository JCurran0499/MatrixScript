package Interpreters;

public abstract class Primitive implements Interpreter {

    public String declaration = null;

    public Primitive solve() {
        return this;
    }

    public abstract String string();

    public abstract boolean equals(Primitive p);
}
