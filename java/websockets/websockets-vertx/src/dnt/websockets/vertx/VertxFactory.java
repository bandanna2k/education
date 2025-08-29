package dnt.websockets.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class VertxFactory
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VertxFactory.class);

    public static Vertx newVertx()
    {
        VertxOptions options = new VertxOptions();
        Vertx vertx = Vertx.vertx(options);


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Executing custom shutdown hook... {}", vertx.hashCode());
            vertx.close();
            LOGGER.info("Executed custom shutdown hook...{}", vertx.hashCode());
        }));
        return vertx;
    }
}
