package Interpreters.Variables;

import Interpreters.Primitive;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

import java.util.TimerTask;
import java.util.Timer;

import Program.MatrixScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class SessionHandler {

    private static final Map<String, LocalDateTime> sessionExpirations = new LinkedHashMap<>(); //Sessions are expired in their order of insertion
    private static final Map<String, Map<String, Primitive>> sessionMap = new HashMap<>();

    private static Logger logger = LoggerFactory.getLogger(MatrixScript.class);


    /* ---------- Session Methods ---------- */

    public static synchronized int createSession(String sessionToken) {
        if (!sessionMap.containsKey(sessionToken)) {
            sessionMap.put(sessionToken, new HashMap<>());
            sessionExpirations.put(sessionToken, LocalDateTime.now().plusHours(12));

            logger.info("new token generated: ~" + sessionToken + "~");
            logger.info(SessionHandler.sessionCount() + " open session" +
                    (SessionHandler.sessionCount() != 1 ? "s" : ""));

            return 0;
        }

        return -1;
    }

    public static synchronized int invalidateSession(String sessionToken) {
        if (sessionMap.containsKey(sessionToken)) {
            sessionMap.remove(sessionToken);
            sessionExpirations.remove(sessionToken);

            logger.info("token ~" + sessionToken + "~ expired");
            logger.info(SessionHandler.sessionCount() + " open session" +
                    (SessionHandler.sessionCount() != 1 ? "s" : ""));

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
                    if (LocalDateTime.now().isAfter(sessionExpirations.get(s)))
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
