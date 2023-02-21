package Interpreters.Variables;

import Interpreters.Primitive;
import java.util.Map;
import spark.Session;

public class VarHandler {

    public static Map<String, Primitive> variables = null;
    public static Session session = null;
    public static boolean api = false;

    public static boolean varExists(String var) {
        if (api)
            return session.attribute(var) != null;
        else
            return variables.containsKey(var);
    }

    public static Primitive getVar(String var) {
        if (api)
            return session.attribute(var);
        else
            return variables.get(var);
    }

    public static void setVar(String varName, Primitive val) {
        if (api)
            session.attribute(varName, val);
        else
            variables.put(varName, val);
    }
}
