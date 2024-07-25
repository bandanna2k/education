package education.vertx;

import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.util.Arrays;

import static io.vertx.core.http.HttpMethod.*;

public class VertxHelloWorld
{
    private static final int PORT = 8888;
    private static final String HELLO_WORLD = STR."""
            {
                "message", "hello-world"
            }
            """;

    public static void main(String[] args)
    {
        new VertxHelloWorld().go();
    }

    private void go()
    {
        VertxOptions options = new VertxOptions();
        final Vertx vertx = Vertx.vertx(options);

        vertx.deployVerticle(new Verticle() {
            @Override
            public Vertx getVertx() {
                return vertx;
            }

            @Override
            public void init(final Vertx vertx, final Context context)
            {

            }

            @Override
            public void start(final Promise<Void> startPromise)
            {
                final Router router = Router.router(vertx);
                router.route(GET, "/hello-world").handler(event -> event
                        .response()
                        .putHeader("Content-Type", "application/json")
                        .send(HELLO_WORLD)
                );
                getVertx().createHttpServer().requestHandler(router).listen(PORT, http ->
                {
                    if (http.succeeded())
                    {
                        startPromise.complete();
                        System.out.println("Vertx started on " + PORT);
                    }
                    else
                    {
                        startPromise.fail(http.cause());
                    }
                });
            }

            @Override
            public void stop(final Promise<Void> promise)
            {
            }
        })
        .onSuccess(event -> System.out.println("Verticles deployed."))
        .onFailure(event -> System.err.println("Failed to deploy. " + event.getMessage()));
    }
}