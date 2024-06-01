package app.api.routes;

import app.api.Route;
import app.api.responses.CommandResponse;
import com.fasterxml.jackson.databind.JsonNode;
import spark.Request;
import spark.Response;

public class HealthRoute extends Route {

    public JsonNode run(Request req, Response res) {
        setCORSHeaders(res);
        setJSONHeader(res);

        return mapper.valueToTree(
            new CommandResponse("OK")
        );
    }
}
