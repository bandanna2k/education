package education.openapi.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import education.openapi.operations.components.ErrorResponse;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface ApiOperation extends Handler<RoutingContext>
{
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

    static String toVertxPath(String jaxRsPath) {
        return jaxRsPath.replaceAll("\\{([^}]+)\\}", ":$1");
    }

    static int tryParseInt(String value)
    {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid path parameter. " + value);
        }
    }

}
