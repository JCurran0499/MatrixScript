package Interpreters.Primitives;

import Interpreters.Interpreter;
import Interpreters.Primitive;

import java.util.ArrayList;
import java.util.List;

public class Tuple extends Primitive {

    private final List<Primitive> pList;

    public Tuple(List<Interpreter> iList) {
        pList = new ArrayList<>();
        for (Interpreter i : iList)
            pList.add(i.solve());
    }

    /* Base Methods */

    public Primitive solve() {
        for (Primitive p : pList) {
            if (p.id().equals("err"))
                return p;
        }

        return this;
    }

    public String id() {
        return "tuple";
    }

    public String string() {
        StringBuilder s = new StringBuilder("(");
        for (Primitive p : pList)
            s.append(p.string()).append(", ");
        s.delete(s.length() - 2, s.length());
        s.append(")");

        return s.toString();
    }

    public int length() {
        return pList.size();
    }

    public boolean equals(Primitive p) {
        if (!id().equals(p.id()))
            return false;

        Tuple t = (Tuple) p;
        if (pList.size() != t.pList.size())
            return false;

        for (int i = 0; i < pList.size(); i++) {
            if (!pList.get(i).equals(t.pList.get(i)))
                return false;
        }

        return true;
    }

    /* Logic Methods */

    public Tuple negate() {
        List<Interpreter> newPList = new ArrayList<>();
        for (int i = pList.size() - 1; i >= 0; i--)
            newPList.add(pList.get(i));

        return new Tuple(newPList);
    }

    public Primitive get(Primitive index) {
        if (index.id().equals("num"))
            return get((Num) index);

        else if (index.id().equals("range"))
            return get((Range) index);

        else if (index.id().equals("tuple"))
            return get((Tuple) index);

        else
            return new Err("invalid 'get' command on tuple");
    }

    private Primitive get(Num index) {
        if (!index.isInteger())
            return new Err("index must be integer");

        int i = index.num().intValue();
        if (i < 0 || i > length())
            return new Err("outside tuple bounds");

        return pList.get(i);

    }

    private Primitive get(Range range) {
        List<Interpreter> newTuple = new ArrayList<>();

        for (int i : range.fullRange()) {
            if (i < 0 || i >= length())
                return new Err("outside tuple bounds");

            newTuple.add(pList.get(i));
        }

        return new Tuple(newTuple);
    }

    private Primitive get(Tuple tuple) {
        List<Interpreter> newTuple = new ArrayList<>();

        for (Primitive p : tuple.pList) {
            if (!p.id().equals("num") || !((Num) p).isInteger())
                return new Err("index must be integer");

            int i = ((Num) p).num().intValue();
            if (i < 0 || i >= length())
                return new Err("outside tuple bounds");

            newTuple.add(pList.get(i));
        }

        return new Tuple(newTuple);
    }
}
