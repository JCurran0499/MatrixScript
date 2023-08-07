package interpreters;

import app.parser.interpreters.primitives.*;
import org.junit.jupiter.api.Test;
import resources.matrix.Matrix;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrimitivesTest {

    Bool b;
    Err e;
    Mat m;
    Num n;
    Range r;
    Tuple t;

    @Test
    public void boolBasic() {
        assertTrue(Bool.of(true).bool());
        assertFalse(Bool.of(false).bool());

        assertTrue(Bool.of(false).not().bool());
        assertFalse(Bool.of(true).not().bool());
    }

    @Test
    public void boolComparison() {
        b = Bool.of(true);
        assertTrue(b.equals(Bool.of(true)));
        assertFalse(b.equals(Bool.of(false)));
        assertFalse(b.equals(Bool.of(true).not()));

        assertFalse(b.equals(new Err("test error")));
        assertFalse(b.equals(new Mat(new Matrix("5 6 7"))));
        assertFalse(b.equals(Null.returnNull()));
        assertFalse(b.equals(new Num(5)));
    }

    @Test
    public void boolString() {
        assertEquals(Bool.of(true).string(), "true");
        assertEquals(Bool.of(false).string(), "false");
        assertEquals(Bool.of(true).not().string(), "false");
    }

    @Test
    public void errComparison() {
        e = new Err("this is a test error message");
        assertTrue(e.equals(new Err("this is a test error message")));
        assertFalse(e.equals(new Err("different message")));
        assertFalse(e.equals(new Err("This is a test error message")));

        assertFalse(e.equals(new Num(3)));
    }

    @Test
    public void errString() {
        e = new Err("this is another test error message");
        assertEquals(e.string(), "Error: this is another test error message");
    }



    @Test
    public void solveReflexive() {
        b = Bool.of(true);
        assertEquals(b.solve(), b);
        b = Bool.of(false);
        assertEquals(b.solve(), b);

        e = new Err("test error message");
        assertEquals(e.solve(), e);

        m = new Mat(new Matrix("1 2 3 ; 4 5 6"));
        assertEquals(m.solve(), m);

        n = new Num(5);
        assertEquals(n.solve(), n);

        assertEquals(Null.returnNull().solve(), Null.returnNull());

        r = new Range(0, 5);
        assertEquals(r.solve(), r);

        t = new Tuple(List.of(b, m, n, r));
        assertEquals(t.solve(), t);
    }
}
