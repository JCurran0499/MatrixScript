package app.parser.interpreters.variables;

import app.parser.interpreters.Primitive;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import java.time.ZonedDateTime;

public class SessionHandler {

    public static final String RUN_TOKEN = "default-run-token";

    private static final Map<String, ZonedDateTime> sessionExpirations = new LinkedHashMap<>(); //Sessions are expired in their order of insertion
    private static final Map<String, Map<String, Primitive>> sessionMap = new HashMap<>();


    /* ---------- Session Methods ---------- */

    public static synchronized int createSession(String sessionToken) {
        if (!sessionMap.containsKey(sessionToken)) {
            sessionMap.put(sessionToken, new HashMap<>());

            if (!sessionToken.equals(RUN_TOKEN))
                sessionExpirations.put(sessionToken, ZonedDateTime.now().plusHours(12));

            return 0;
        }

        return -1;
    }

    public static synchronized int invalidateSession(String sessionToken) {
        if (sessionMap.containsKey(sessionToken)) {
            sessionMap.remove(sessionToken);

            if (!sessionToken.equals(RUN_TOKEN)) {
                sessionExpirations.remove(sessionToken);
            }

            return 0;
        }

        return -1;
    }

    public static boolean validSession(String sessionToken) {
        return sessionMap.containsKey(sessionToken);
    }

    public static int sessionCount() {
        return sessionMap.size();
    }

    public static List<String> tokens() {
        return sessionMap.keySet().stream().toList();
    }

    public static ZonedDateTime getExpiration(String sessionToken) {
        return sessionExpirations.get(sessionToken);
    }

    /* ---------- Variable Methods ---------- */

    public static boolean varExists(String sessionToken, String var) {
        return sessionMap.get(sessionToken).containsKey(var);
    }

    public static Primitive getVar(String sessionToken, String var) {
        return sessionMap.get(sessionToken).get(var);
    }

    public static void setVar(String sessionToken, String varName, Primitive val) {
        sessionMap.get(sessionToken).put(varName, val);
    }

    /* ---------- Session Manager Thread ---------- */

    public static void initiateSessionManager() {
        TimerTask task = new TimerTask() {
            public void run() {
                List<String> invalidSessions = new ArrayList<>();
                for (String s : sessionExpirations.keySet()) {
                    if (ZonedDateTime.now().isAfter(sessionExpirations.get(s)))
                        invalidSessions.add(s);
                    else break;
                }

                for (String s : invalidSessions)
                    invalidateSession(s);
            }
        };

        final long SECOND = 1000;

        Timer manager = new Timer("Session Manager");
        manager.schedule(task, 0, 60 * SECOND);
    }

}
