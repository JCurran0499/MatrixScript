package app.api.routes;

import app.api.Route;
import app.api.responses.CommandResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;

@AllArgsConstructor
public class HealthRoute extends Route {

    private final ObjectMapper mapper;

    public JsonNode run(Request req, Response res) {
        setCORSHeaders(res);
        setJSONHeader(res);

        return mapper.valueToTree(
            new CommandResponse("OK")
        );
    }
}
