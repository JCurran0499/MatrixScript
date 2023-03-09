package Program;

import java.util.Scanner;
import java.util.UUID;

import Interpreters.*;
import Interpreters.Primitives.Null;
import Parser.Parser;
import Interpreters.Variables.SessionHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;
import spark.Response;

import static spark.Spark.*;

public class MatrixScape {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("run"))
            runCommands();
        else {
            runAPI();
        }
    }

    public static void runAPI() {
        Dotenv dotenv = Dotenv.load();
        Logger logger = LoggerFactory.getLogger(MatrixScape.class);
        ObjectMapper mapper = new ObjectMapper();

        port(4567);

        options("/*", (req, res) -> {
            setCORSHeaders(res, dotenv.get("FRONTEND"));
            return "OK";
        });

        get("/token", (req, res) -> {
            setCORSHeaders(res, dotenv.get("FRONTEND"));
            res.header("Content-Type", "application/json");

            String sessionToken = UUID.randomUUID().toString();
            int success = SessionHandler.createSession(sessionToken);
            if (success == -1) {
                logger.error("500 ERROR - token generation error on server side");
                halt(500, "error generating token");
            }

            logger.info("new token generated: ~" + sessionToken + "~");
            logger.info(SessionHandler.sessionCount() + " open session" +
                    (SessionHandler.sessionCount() != 1 ? "s" : ""));

            return mapper.readTree(String.format("{\"sessionToken\": \"%s\"}", sessionToken));
        });

        delete("/token/:token", (req, res) -> {
            setCORSHeaders(res, dotenv.get("FRONTEND"));

            String sessionToken = req.params(":token");
            int success = SessionHandler.invalidateSession(sessionToken);
            if (success == -1) {
                logger.error("404 ERROR - tried to delete invalid token");
                halt(404, "invalid session token");
            }

            logger.info("token ~" + sessionToken + "~ deleted");
            logger.info(SessionHandler.sessionCount() + " open session" +
                    (SessionHandler.sessionCount() != 1 ? "s" : ""));

            return "OK";
        });

        post("/", (req, res) -> {
            setCORSHeaders(res, dotenv.get("FRONTEND"));
            res.header("Content-Type", "application/json");

            String sessionToken = req.queryParams("token");
            if (sessionToken == null) {
                logger.info("no session token provided - using session id instead");
                sessionToken = req.session().id();
                SessionHandler.createSession(sessionToken);
            }

            if (!SessionHandler.validSession(sessionToken)) {
                logger.error("401 ERROR - invalid session token provided");
                halt(401, "invalid session token");
            }
            SessionHandler.setSession(sessionToken);

            JsonNode body;
            try {
                body = mapper.readTree(req.body()).get("command");
            } catch (Exception e) {
                logger.error("error in request body format");
                halt(400, "invalid request body format");
                return "";
            }

            String command = body.asText();
            Primitive result = execute(command);

            String response;
            if (result.id().equals("mat") && result.printValue) {
                String matString = result.string().replaceAll("\n", "n");
                response = String.format("{\"response\": {\"matrix\": \"%s\"}}", matString);
                return mapper.readTree(response);
            }
            else if (result.printValue) {
                response = String.format("{\"response\": \"%s\"}", result.string());
                return mapper.readTree(response);
            }
            else {
                result.printValue = true;
                response = String.format("{\"response\": \"%s\"}", "");
                return mapper.readTree(response);
            }
        });
    }

    public static void runCommands() {
        Scanner scanner = new Scanner(System.in);

        boolean end = true;
        while (end) {
            System.out.print(">> ");
            String command = scanner.nextLine().strip();

            if (command.equals("quit") || command.equals("exit"))
                end = false;
            else {

                Primitive result = execute(command);
                if (!result.id().equals("null") && result.printValue)
                    System.out.println(result.string() + "\n");
                else
                    result.printValue = true;
            }
        }

        System.out.println();
    }

    private static Primitive execute(String command) {
        if (command.contains("//"))
            command = command.substring(0, command.indexOf("//")).stripTrailing();

        if (command.equals("quit") || command.equals("exit")) {
            return Null.returnNull();
        }

        return Parser.parse(command).solve();
    }

    private static void setCORSHeaders(Response res, String frontend) {
        res.header("Access-Control-Allow-Methods", "POST,GET,DELETE");
        res.header("Access-Control-Allow-Origin", "http://" + frontend); //dotenv.get("FRONTEND"));
        res.header("Access-Control-Allow-Credentials", "true");
        res.header("Access-Control-Allow-Headers", "content-type");
    }
}
