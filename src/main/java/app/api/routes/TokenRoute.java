package app.api.routes;

import app.api.Route;
import app.api.responses.ErrorResponse;
import app.api.responses.TokenResponse;
import app.parser.interpreters.variables.SessionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import spark.Request;
import spark.Response;

import java.util.UUID;

import static spark.Spark.halt;

@AllArgsConstructor
public class TokenRoute extends Route {

    private final ObjectMapper mapper;
    private final Logger logger;

    public JsonNode run(Request req, Response res) throws JsonProcessingException {
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
    }
}
