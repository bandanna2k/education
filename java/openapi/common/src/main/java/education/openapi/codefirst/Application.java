package education.openapi.codefirst;

import education.openapi.codefirst.operations.ApiOperation;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import javax.ws.rs.*;
import java.lang.reflect.Method;
import java.util.Arrays;

import static education.openapi.codefirst.operations.ApiOperation.respondError;

public class Application
{
    private final Vertx vertx;
    private HttpServer server;

    private final ApiOperation[] operations;

    public Application(Vertx vertx, ApiOperation... operations) {
        this.vertx = vertx;
        this.operations = operations;
    }

    public Future<Void> start(int port) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().failureHandler(Application::handleFailure);

        Arrays.stream(operations).forEach(operation -> addHandler(router, operation));

        Promise<Void> promise = Promise.promise();
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(port)
                .onSuccess(httpServer -> {
                    this.server = httpServer;
                    promise.complete();
                })
                .onFailure(promise::fail);
        return promise.future();
    }

    private static void handleFailure(RoutingContext ctx)
    {
        Throwable failure = ctx.failure();
        if (failure != null) {
            respondError(ctx, 404, "BAD_REQUEST", failure.getMessage());
        } else {
            respondError(ctx, 500, "SERVER_ERROR", "An unexpected error occurred");
        }
    }

    private void addHandler(Router router, ApiOperation operation)
    {
        Class<?> operationClass = operation.getClass().getInterfaces()[0];
        Path pathAnnotation = operationClass.getAnnotation(Path.class);
        String path = ApiOperation.toVertxPath(pathAnnotation.value());

        for (Method method : operationClass.getMethods()) {
            if (method.isAnnotationPresent(GET.class))    { router.get(path).handler(operation);    return; }
            if (method.isAnnotationPresent(POST.class))   { router.post(path).handler(operation);   return; }
            if (method.isAnnotationPresent(PUT.class))    { router.put(path).handler(operation);    return; }
            if (method.isAnnotationPresent(DELETE.class)) { router.delete(path).handler(operation); return; }
            if (method.isAnnotationPresent(PATCH.class))  { router.patch(path).handler(operation);  return; }
        }
    }

    public Future<Void> stop() {
        if (server == null) return Future.succeededFuture();
        return server.close().mapEmpty();
    }

    public int actualPort() {
        if (server == null) throw new IllegalStateException("Server not started");
        return server.actualPort();
    }
}
