package App.Parser.Interpreters;

public abstract class Primitive implements Interpreter {

    public boolean printValue = true;

    public Primitive solve() {
        return this;
    }

    public abstract String string();

    public abstract boolean equals(Primitive p);
}
