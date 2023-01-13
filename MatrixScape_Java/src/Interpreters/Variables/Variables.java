package Interpreters.Variables;

import Interpreters.Primitive;

import java.util.Map;
import java.util.TreeMap;

public class Variables {

    private static final Map<String, Primitive> variables = new TreeMap<>();

    public static Map<String, Primitive> variables() {
        return variables;
    }

    public static boolean containsVar(String var) {
        return variables.containsKey(var);
    }

    public static Primitive getVar(String var) {
        return variables.get(var);
    }
}
