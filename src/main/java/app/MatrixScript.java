package app;

import java.util.Scanner;
import java.util.UUID;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Err;
import app.parser.interpreters.primitives.Mat;
import app.parser.interpreters.primitives.Null;
import app.parser.Parser;
import app.parser.interpreters.variables.SessionHandler;
import app.responses.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.cdimascio.dotenv.Dotenv;
import resources.aws.AwsService;

import static spark.Spark.*;

public class MatrixScript {

    private static final Dotenv env = Dotenv.load();
    private static final Logger logger = LoggerFactory.getLogger(MatrixScript.class);
    private static final ObjectMapper mapper = new ObjectMapper();


    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("run"))
            runCommands();
        else {
            runAPI();
        }
    }

    private static void runAPI() {
        SessionHandler.initiateSessionManager();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        port(4567);

        // --- Public Endpoints --- //

        options("/*", (req, res) -> {
            setCORSHeaders(res);
            return "OK";
        });

        get("/health", (req, res) -> {
            setCORSHeaders(res);
            return "OK";
        });

        get("/token", (req, res) -> {
            setCORSHeaders(res);
            setJSONHeader(res);

            String sessionToken = UUID.randomUUID().toString();
            int success = SessionHandler.createSession(sessionToken);
            if (success == -1) {
                logger.error("500 ERROR - token generation error on server side");
                halt(500, mapper.writeValueAsString(
                    new ErrorResponse("error generating token")
                ));
            }

            return mapper.valueToTree(
                new TokenResponse(null, sessionToken)
            );
        });

        post("/", (req, res) -> {
            setCORSHeaders(res);
            setJSONHeader(res);

            String sessionToken = req.queryParams("token");
            if (sessionToken == null) {
                logger.error("401 ERROR - no session token provided");
                halt(401, mapper.writeValueAsString(
                    new ErrorResponse("no session token")
                ));
            }
            if (!SessionHandler.validSession(sessionToken)) {
                logger.error("401 ERROR - invalid session token provided");
                halt(401, mapper.writeValueAsString(
                    new ErrorResponse("invalid session token")
                ));
            }

            JsonNode body;
            try {
                body = mapper.readTree(req.body()).get("command");
            } catch (Exception e) {
                logger.error("error in request body format");
                halt(400, mapper.writeValueAsString(
                    new ErrorResponse("invalid request body format")
                ));
                return "";
            }

            String command = body.asText();
            Primitive result;
            try {
                result = execute(sessionToken, command);
            } catch (Exception e) {
                AwsService.publishError(env.get("SNS"), command, e);
                halt(500, mapper.writeValueAsString(
                    new ErrorResponse("unexpected internal server error")
                ));
                return "";
            }

            return commandJson(result);
        });


        // --- Private Endpoints --- //

        before("/private/*", (req, res) -> {
            if (!AwsService.authorize(req.headers("Authorization")))
                halt(403, mapper.writeValueAsString(
                    new ErrorResponse("access forbidden - invalid key")
                ));
        });

        path("/private", () -> {
            delete("/token/:token", (req, res) -> {
                setJSONHeader(res);

                int success = SessionHandler.invalidateSession(req.params(":token"));
                if (success == -1) {
                    logger.error("404 ERROR - tried to delete invalid token");
                    halt(404, mapper.writeValueAsString(
                        new ErrorResponse("invalid session token")
                    ));
                }

                return mapper.valueToTree(
                    new TokenResponse("DELETED", req.params(":token"))
                );
            });

            get("/list-sessions", (req, res) -> {
                setJSONHeader(res);

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
        System.out.println("========================================================================");
        System.out.println("\n");

        SessionHandler.createSession(SessionHandler.RUN_TOKEN);
        Scanner scanner = new Scanner(System.in);

        boolean end = true;
        while (end) {
            System.out.print(">> ");
            String command = scanner.nextLine().strip();

            if (command.equals("quit()") || command.equals("exit()"))
                end = false;
            else {

                Primitive result = execute(SessionHandler.RUN_TOKEN, command);
                if (!Null.is(result) && result.printValue)
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

        return Parser.parse(sessionToken, command).solve();
    }


    /* Helper Methods */

    private static JsonNode commandJson(Primitive result) {
        if (Mat.is(result) && result.printValue) {
            String matString = result.string().replaceAll("\n", "n");
            return mapper.valueToTree(
                new CommandResponse("success", null, matString, null)
            );
        }
        else if (Err.is(result) && result.printValue) {
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
    }

    private static void setCORSHeaders(spark.Response res) {
        res.header("Access-Control-Allow-Methods", "POST,GET,DELETE");
        res.header("Access-Control-Allow-Origin", env.get("FRONTEND"));
        res.header("Access-Control-Allow-Credentials", "true");
        res.header("Access-Control-Allow-Headers", "content-type");
    }

    private static void setJSONHeader(spark.Response res) {
        res.header("Content-Type", "application/json");
    }
}
