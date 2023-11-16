package app.api.routes.priv;

import app.api.Route;
import app.api.responses.ListSessionsResponse;
import app.parser.interpreters.variables.SessionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;

import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
public class ListSessionsRoute extends Route {

    private final ObjectMapper mapper;

    public JsonNode run(Request req, Response res) throws JsonProcessingException {
        setJSONHeader(res);

        int sessionCount = SessionHandler.sessionCount();

        List<String> tokens = SessionHandler.tokens();
        ListSessionsResponse.SessionJson[] sessions = new ListSessionsResponse.SessionJson[sessionCount];

        for (int i = 0; i < sessionCount; i++) {
            String s = tokens.get(i);
            sessions[i] = new ListSessionsResponse.SessionJson(
                s, SessionHandler.getExpiration(s).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"))
            );
        }

        return mapper.valueToTree(
            new ListSessionsResponse(sessionCount, sessions)
        );
    }
}
