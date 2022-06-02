package Interpreters;

public abstract class Primitive implements Interpreter {

    public Primitive solve() {
        return this;
    }

    public abstract String string();
}
