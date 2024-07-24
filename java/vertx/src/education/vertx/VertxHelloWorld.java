package education.vertx;

import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class VertxHelloWorld
{
    public static final int PORT = 8888;

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
                getVertx().createHttpServer().requestHandler(request ->
                {
                    request.response()
                            .putHeader("content-type", "text/plain")
                            .end("Hello World");
                }).listen(PORT, http ->
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
            public void stop(final Promise<Void> promise) throws Exception {

            }
        });
    }
}