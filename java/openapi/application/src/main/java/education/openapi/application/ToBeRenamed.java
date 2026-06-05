package education.openapi.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import education.openapi.model.ErrorResponse;
import io.vertx.ext.web.RoutingContext;

public interface ToBeRenamed
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
        ErrorResponse error = new ErrorResponse().code(code).message(message);
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
