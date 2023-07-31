package App;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.UUID;
import App.Parser.Interpreters.Primitive;
import App.Parser.Interpreters.Primitives.Null;
import App.Parser.Parser;
import App.Parser.Interpreters.Variables.SessionHandler;
import App.ResponsesJson.CommandResponse;
import App.ResponsesJson.SessionListResponse;
import App.ResponsesJson.TokenResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.cdimascio.dotenv.Dotenv;
//import software.amazon.awssdk.*;

import static spark.Spark.*;

public class MatrixScript {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("run"))
            runCommands();
        else {
            runAPI();
        }
    }

    private static void runAPI() {
        Dotenv env = Dotenv.load();
        Logger logger = LoggerFactory.getLogger(MatrixScript.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        SessionHandler.initiateSessionManager();

        port(4567);

        // --- Public Endpoints --- //

        options("/*", (req, res) -> {
            setCORSHeaders(res, env);
            return "OK";
        });

        get("/health", (req, res) -> {
            setCORSHeaders(res, env);
            return "OK";
        });

        get("/token", (req, res) -> {
            setCORSHeaders(res, env);
            setJSONHeader(res);

            String sessionToken = UUID.randomUUID().toString();
            int success = SessionHandler.createSession(sessionToken);
            if (success == -1) {
                logger.error("500 ERROR - token generation error on server side");
                halt(500, "error generating token");
            }

            return mapper.valueToTree(
                new TokenResponse(sessionToken)
            );
        });

        post("/", (req, res) -> {
            setCORSHeaders(res, env);
            setJSONHeader(res);

            String sessionToken = req.queryParams("token");
            if (sessionToken == null) {
                logger.error("401 ERROR - no session token provided");
                halt(401, "no session token");
            }
            if (!SessionHandler.validSession(sessionToken)) {
                logger.error("401 ERROR - invalid session token provided");
                halt(401, "invalid session token");
            }

            JsonNode body;
            try {
                body = mapper.readTree(req.body()).get("command");
            } catch (Exception e) {
                logger.error("error in request body format");
                halt(400, "invalid request body format");
                return "";
            }

            String command = body.asText();
            Primitive result = execute(sessionToken, command);

            String response;
            if (result.id().equals("mat") && result.printValue) {
                String matString = result.string().replaceAll("\n", "n");
                return mapper.valueToTree(
                    new CommandResponse("success", null, matString, null)
                );
            }
            else if (result.id().equals("err") && result.printValue) {
                return mapper.valueToTree(
                    new CommandResponse("error", null, null, result.string())
                );
            }
            else if (result.printValue) {
                return mapper.valueToTree(
                    new CommandResponse("success", result.string(), null, null)
                );
            }
            else {
                result.printValue = true;
                return mapper.valueToTree(
                    new CommandResponse("success", "", null, null)
                );
            }
        });


        // --- Private Endpoints --- //

        path("/private", () -> {
            delete("/token/:token", (req, res) -> {
                int success = SessionHandler.invalidateSession(req.params(":token"));
                if (success == -1) {
                    logger.error("404 ERROR - tried to delete invalid token");
                    halt(404, "invalid session token");
                }

                return "OK";
            });

            get("/list-sessions", (req, res) -> {
                setJSONHeader(res);

                StringBuilder sessionList = new StringBuilder(
                    "{\"sessionCount\": " + SessionHandler.sessionCount() + ", \"sessions\": ["
                );

                for (String t : SessionHandler.tokens()) {
                    sessionList.append(String.format(
                        "{\"token\": \"%s\", \"expiration\": \"%s\"},",
                        t, SessionHandler.getExpiration(t).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    );
                }
                if (SessionHandler.sessionCount() > 0) {
                    sessionList.deleteCharAt(sessionList.length() - 1);
                }
                sessionList.append("]}");

                return mapper.valueToTree(
                    new SessionListResponse(SessionHandler.sessionCount(), SessionHandler.tokens())
                );
            });
        });
    }

    private static void runCommands() {
        System.out.println("\n\n");
        System.out.println("|\\   /|  / \\ |_   _||  _|  | | \\ \\/ / / __|  / _| |  _|  | | | -  ||_   _|");
        System.out.println("| \\ / | / _ \\  | |  |   \\  | |  |  |  \\__ \\ | |_  |   \\  | | |  _/   | |");
        System.out.println("|_| |_|/_/ \\_\\ |_|  |_||_| |_| /_/\\_\\ |___/  \\__| |_||_| |_| |_|     | |");
        System.out.println("\n\n");

        SessionHandler.createSession(SessionHandler.RUN_TOKEN);
        Scanner scanner = new Scanner(System.in);

        boolean end = true;
        while (end) {
            System.out.print(">> ");
            String command = scanner.nextLine().strip();

            if (command.equals("quit") || command.equals("exit"))
                end = false;
            else {

                Primitive result = execute(SessionHandler.RUN_TOKEN, command);
                if (!result.id().equals("null") && result.printValue)
                    System.out.println(result.string() + "\n");
                else
                    result.printValue = true;
            }
        }

        System.out.println();
    }

    private static Primitive execute(String sessionToken, String command) {
        if (command.contains("//"))
            command = command.substring(0, command.indexOf("//")).stripTrailing();

        if (command.equals("quit") || command.equals("exit")) {
            return Null.returnNull();
        }

        return Parser.parse(sessionToken, command).solve();
    }

    private static void setCORSHeaders(spark.Response res, Dotenv env) {
        res.header("Access-Control-Allow-Methods", "POST,GET,DELETE");
        res.header("Access-Control-Allow-Origin", env.get("FRONTEND"));
        res.header("Access-Control-Allow-Credentials", "true");
        res.header("Access-Control-Allow-Headers", "content-type");
    }

    private static void setJSONHeader(spark.Response res) {
        res.header("Content-Type", "application/json");
    }
}
