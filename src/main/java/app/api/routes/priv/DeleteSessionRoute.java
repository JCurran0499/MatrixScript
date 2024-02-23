package app.api.routes.priv;

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

import static spark.Spark.halt;

@AllArgsConstructor
public class DeleteSessionRoute extends Route {

    private final ObjectMapper mapper;
    private final Logger logger;

    public JsonNode run(Request req, Response res) throws JsonProcessingException {
        setJSONHeader(res);

        String sessionToken = req.params(":token");
        int success = SessionHandler.invalidateSession(sessionToken);
        if (success == -1) {
            logger.error("404 ERROR - tried to delete invalid token");
            halt(404, mapper.writeValueAsString(
                new ErrorResponse("invalid session token")
            ));
        }

        logger.info("token ~" + sessionToken + "~ expired");
        logger.info(SessionHandler.sessionCount() + " open session" +
            (SessionHandler.sessionCount() != 1 ? "s" : ""));

        return mapper.valueToTree(
            new TokenResponse("DELETED", req.params(":token"))
        );
    }
}
