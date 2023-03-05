package Interpreters.Variables;

import Interpreters.Primitive;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spark.Session;

public class VarHandler {

    public static final Map<String, Map<String, Primitive>> variableMap = new TreeMap<>();
    public static Map<String, Primitive> variables = null;

    public static int createSession(String sessionToken) {
        if (!variableMap.containsKey(sessionToken)) {
            variableMap.put(sessionToken, new TreeMap<>());
            return 0;
        }

        return -1;
    }

    public static int invalidateSession(String sessionToken) {
        if (variableMap.containsKey(sessionToken)) {
            variableMap.remove(sessionToken);
            return 0;
        }

        return -1;
    }


    public static boolean validSession(String sessionToken) {
        return variableMap.containsKey(sessionToken);
    }

    public static boolean varExists(String var) {
        return variables.containsKey(var);
    }

    public static Primitive getVar(String var) {
        return variables.get(var);
    }

    public static void setVar(String varName, Primitive val) {
        variables.put(varName, val);
    }
}
