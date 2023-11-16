package app.api.routes;

import app.api.Route;
import app.api.responses.CommandResponse;
import app.api.responses.ErrorResponse;
import app.parser.Parser;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Declare;
import app.parser.interpreters.primitives.Err;
import app.parser.interpreters.primitives.Mat;
import app.parser.interpreters.variables.SessionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import resources.aws.AwsService;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

@AllArgsConstructor
public class CommandRoute extends Route {

    private final ObjectMapper mapper;
    private final Logger logger;

    public JsonNode run(Request req, Response res) throws JsonProcessingException {
        setCORSHeaders(res);
        setJSONHeader(res);

        String sessionToken = req.queryParams("token");
        if (sessionToken == null) {
            logger.error("400 ERROR - no session token provided");
            halt(400, mapper.writeValueAsString(
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
            return null;
        }

        String command = body.asText();
        Primitive result;
        try {
            result = execute(sessionToken, command);
        } catch (Exception e) {
            AwsService.publishError(System.getenv("SNS"), command, e);
            halt(500, mapper.writeValueAsString(
                new ErrorResponse("unexpected internal server error")
            ));
            return null;
        }

        return commandJson(result, mapper);
    }

    private static Primitive execute(String sessionToken, String command) {
        if (command.contains("//"))
            command = command.substring(0, command.indexOf("//")).stripTrailing();

        return Parser.parse(sessionToken, command).solve();
    }

    private static JsonNode commandJson(Primitive result, ObjectMapper mapper) {
        if (Mat.is(result)) {
            String matString = result.string().replaceAll("\n", "n");
            return mapper.valueToTree(
                new CommandResponse("success", null, matString, null)
            );
        }
        else if (Err.is(result)) {
            return mapper.valueToTree(
                new CommandResponse("error", null, null, result.string())
            );
        }
        else if (Declare.is(result)) {
            return mapper.valueToTree(
                new CommandResponse("success", "", null, null)
            );
        }
        else {
            return mapper.valueToTree(
                new CommandResponse("success", result.string(), null, null)
            );
        }
    }
}
