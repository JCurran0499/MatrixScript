package app;

import java.util.Scanner;
import java.util.UUID;
import app.parser.Primitive;
import app.parser.primitives.Err;
import app.parser.primitives.Mat;
import app.parser.primitives.Null;
import app.parser.parser.Parser;
import app.parser.interpreters.variables.SessionHandler;
import app.responses.CommandResponse;
import app.responses.SessionListResponse;
import app.responses.TokenResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.regions.Region;
import spark.Request;

import static spark.Spark.*;

public class MatrixScript {

    private static final String ERROR_MESSAGE = "An error has occurred while a user was using MatrixScript.\n\n" +
        "The command causing the error was \"%s\".\nError message:\n%s";

    private static final String ERROR_SUBJECT = "[ALERT] MatrixScript Error";


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

        SnsClient sns = SnsClient.builder()
            .region(Region.US_EAST_1)
            .build();

        SsmClient ssm = SsmClient.builder()
            .region(Region.US_EAST_1)
            .build();

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
            Primitive result;
            try {
                result = execute(sessionToken, command);
            } catch (Exception e) {
                sns.publish(PublishRequest.builder()
                    .topicArn(env.get("SNS"))
                    .message(String.format(ERROR_MESSAGE, command, e.getLocalizedMessage()))
                    .subject(ERROR_SUBJECT)
                    .build()
                );
                halt(500, "Unexpected internal server error");
                return "";
            }

            return commandJson(mapper, result);
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
                authorize(ssm, req);

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

            if (command.equals("quit") || command.equals("exit"))
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

    private static JsonNode commandJson(ObjectMapper mapper, Primitive result) {
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

    private static void setCORSHeaders(spark.Response res, Dotenv env) {
        res.header("Access-Control-Allow-Methods", "POST,GET,DELETE");
        res.header("Access-Control-Allow-Origin", env.get("FRONTEND"));
        res.header("Access-Control-Allow-Credentials", "true");
        res.header("Access-Control-Allow-Headers", "content-type");
    }

    private static void setJSONHeader(spark.Response res) {
        res.header("Content-Type", "application/json");
    }

    private static void authorize(SsmClient ssm, Request req) {
        GetParameterResponse response = ssm.getParameter(GetParameterRequest.builder()
            .name("matrixscript_private_endpoint_key")
            .withDecryption(true)
            .build()
        );

        if (!req.headers("Authorization").equals("Bearer " + response.parameter().value()))
            halt(403, "Access forbidden: invalid key");
    }
}
