package app.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import spark.Request;
import spark.Response;

public abstract class Route {

    protected static final ObjectMapper mapper = new ObjectMapper();
    protected static final Dotenv env = Dotenv.load();

    public abstract JsonNode run(Request req, Response res) throws JsonProcessingException;
    protected static void setCORSHeaders(Response res) {
        res.header("Access-Control-Allow-Methods", "POST,GET,DELETE");
        res.header("Access-Control-Allow-Origin", env.get("FRONTEND"));
        res.header("Access-Control-Allow-Credentials", "true");
        res.header("Access-Control-Allow-Headers", "content-type");
    }

    protected static void setJSONHeader(Response res) {
        res.header("Content-Type", "application/json");
    }
}
