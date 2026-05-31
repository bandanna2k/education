package education.openapi.codefirst.operations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import education.openapi.codefirst.components.ErrorResponse;
import io.vertx.ext.web.RoutingContext;

public interface ApiOperation<Result, Body>
{
    default void handle(RoutingContext ctx) {
        try {
            Body req = MAPPER.readValue(ctx.body().asString(), new TypeReference<>() {});
            Result result = execute(req);
            respondJson(ctx, 200, result);
        } catch (Exception e) {
            if (e instanceof InsufficientFundsException) {
                respondError(ctx, 400, "INSUFFICIENT_FUNDS", e.getMessage());
            } else {
                respondError(ctx, 400, "BAD_REQUEST", e.getMessage());
            }
        }
    }

    Result execute(Body body);

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
        ErrorResponse error = new ErrorResponse(code, message);
        respondJson(ctx, status, error);
    }
}
