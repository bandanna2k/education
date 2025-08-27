package education.vertx;

import io.vertx.core.*;
import io.vertx.ext.web.Router;

import static io.vertx.core.http.HttpMethod.GET;

public class VertxHelloWorld
{
    private static final int PORT = 8888;
    private static final String HELLO_WORLD = """
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
            public Future<?> deploy(Context context) throws Exception {
                return null;
            }

            @Override
            public Future<?> undeploy(Context context) throws Exception {
                return Verticle.super.undeploy(context);
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
                getVertx().createHttpServer().requestHandler(router).listen(PORT)
                        .onSuccess(unused -> {
                            System.out.println("Vertx started on " + PORT);
                            startPromise.complete();
                        })
                        .onFailure(startPromise::fail);
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