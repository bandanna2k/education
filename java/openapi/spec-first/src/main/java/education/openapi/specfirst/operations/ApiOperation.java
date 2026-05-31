package education.openapi.specfirst.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import education.openapi.specfirst.generated.model.ErrorResponse;
import io.vertx.ext.web.RoutingContext;

public interface ApiOperation
{
    void handle(RoutingContext ctx);


    ObjectMapper MAPPER = new ObjectMapper();

    static void respondJson(RoutingContext ctx, int status, Object body) {
        try {
            String json = MAPPER.writeValueAsString(body);
            ctx.response()
                    .setStatusCode(status)
                    .putHeader("Content-Type", "application/json")
                    .end(json);
        } catch (Exception e) {
            ctx.response().setStatusCode(500).end("Internal error");
        }
    }

    static void respondError(RoutingContext ctx, int status, String code, String message) {
        ErrorResponse error = new ErrorResponse().code(code).message(message);
        respondJson(ctx, status, error);
    }
}
