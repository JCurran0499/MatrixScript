package Interpreters.Variables;

import Interpreters.Primitive;

import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;

public class SessionHandler {

    private static final Map<String, Map<String, Primitive>> sessionMap = new TreeMap<>();
    private static Map<String, Primitive> variables = new HashMap<>(); //HashMap is faster, so it is important here


    /* ---------- Session Methods ---------- */

    public static int createSession(String sessionToken) {
        if (!sessionMap.containsKey(sessionToken)) {
            sessionMap.put(sessionToken, new HashMap<>());
            return 0;
        }

        return -1;
    }

    public static int invalidateSession(String sessionToken) {
        if (sessionMap.containsKey(sessionToken)) {
            sessionMap.remove(sessionToken);
            return 0;
        }

        return -1;
    }

    public static boolean validSession(String sessionToken) {
        return sessionMap.containsKey(sessionToken);
    }

    public static void setSession(String sessionToken) {
        variables = sessionMap.get(sessionToken);
    }

    public static int sessionCount() {
        return sessionMap.size();
    }

    /* ---------- Variable Methods ---------- */

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
