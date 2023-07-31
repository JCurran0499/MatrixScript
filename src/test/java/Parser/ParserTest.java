package Parser;

import static org.junit.jupiter.api.Assertions.*;

import App.Parser.Interpreters.Primitive;
import App.Parser.Interpreters.Primitives.Num;
import App.Parser.Parser;
import org.junit.jupiter.api.*;
import App.Parser.Interpreters.Variables.SessionHandler;

public class ParserTest {

    private static final String t = SessionHandler.RUN_TOKEN;

    @BeforeEach
    public void before() {
        SessionHandler.createSession(t);
    }

    @AfterEach
    public void after() {
        SessionHandler.invalidateSession(t);
    }


    @Test
    public void firstStep() {
        Parser.parse(t, "x = 5").solve();
        Primitive i = Parser.parse(t, "x").solve();
        assertTrue(
            i.equals(new Num(5))
        );
    }

    @Test
    public void secondStep() {
        Primitive i = Parser.parse(t, "x").solve();
        assertFalse(
            i.equals(new Num(5))
        );
    }
}
