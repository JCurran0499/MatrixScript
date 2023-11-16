package app.api;

import app.MatrixScript;
import app.api.responses.ErrorResponse;
import app.api.routes.CommandRoute;
import app.api.routes.HealthRoute;
import app.api.routes.TokenRoute;
import app.api.routes.priv.DeleteSessionRoute;
import app.api.routes.priv.ListSessionsRoute;
import app.parser.interpreters.variables.SessionHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources.aws.AwsService;

import static spark.Spark.*;
import static spark.Spark.get;

public class Api {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(MatrixScript.class);

    private static final Route healthRoute = new HealthRoute(mapper);
    private static final Route tokenRoute = new TokenRoute(mapper, logger);
    private static final Route commandRoute = new CommandRoute(mapper, logger);
    private static final Route deleteSessionRoute = new DeleteSessionRoute(mapper, logger);
    private static final Route listSessionsRoute = new ListSessionsRoute(mapper);

    public static void runAPI() {
        SessionHandler.initiateSessionManager();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        port(4567);

        // --- Public Endpoints --- //

        options("/*", (req, res) -> {
            Route.setCORSHeaders(res);
            return "OK";
        });

        get("/health", healthRoute::run);

        post("/token", tokenRoute::run);

        post("/", commandRoute::run);


        // --- Private Endpoints --- //

        before("/private/*", (req, res) -> {
            if (!AwsService.authorize(req.headers("Authorization"))) {
                Route.setJSONHeader(res);

                halt(403, mapper.writeValueAsString(
                    new ErrorResponse("access forbidden - invalid key")
                ));
            }
        });

        path("/private", () -> {
            delete("/token/:token", deleteSessionRoute::run);

            get("/list-sessions", listSessionsRoute::run);
        });
    }
}
