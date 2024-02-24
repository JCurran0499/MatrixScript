package app.api.routes;

import app.api.Route;
import app.api.responses.ErrorResponse;
import app.api.responses.TokenResponse;
import app.parser.interpreters.variables.SessionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.UUID;

import static spark.Spark.halt;

public class TokenRoute extends Route {

    private final Logger logger = LoggerFactory.getLogger(TokenRoute.class);

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

        logger.info("new token generated: ~" + sessionToken + "~");
        logger.info(SessionHandler.sessionCount() + " open session" +
            (SessionHandler.sessionCount() != 1 ? "s" : ""));

        return mapper.valueToTree(
            new TokenResponse(null, sessionToken)
        );
    }
}
