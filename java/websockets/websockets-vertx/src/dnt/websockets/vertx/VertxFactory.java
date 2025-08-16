package dnt.websockets.vertx;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class VertxFactory
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VertxFactory.class);

    public static Vertx newVertx()
    {
        Vertx vertx = Vertx.vertx();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Executing custom shutdown hook... {}", vertx.hashCode());
            vertx.close();
            LOGGER.info("Executing custom shutdown hook...{}", vertx.hashCode());
        }));
        return vertx;
    }
}
