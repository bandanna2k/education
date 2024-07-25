package casestudy.bank.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

import static io.vertx.core.http.HttpMethod.GET;

public class BankVerticle extends AbstractVerticle
{
    private static final int PORT = 8888;

    public BankVerticle(final Vertx vertx)
    {
        this.vertx = vertx;
    }

    @Override
    public Vertx getVertx()
    {
        return this.vertx;
    }

    @Override
    public void start(final Promise<Void> startPromise)
    {
        final Router router = Router.router(getVertx());
        router.route(GET, "/hello-world").handler(event -> event
                .response()
                .putHeader("Content-Type", "application/json")
                .send("{}")
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
}
