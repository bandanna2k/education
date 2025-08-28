package dnt.websockets.integration.vertx.dsl;

import dnt.websockets.integration.vertx.WebSocketClientDriver;
import dnt.websockets.integration.vertx.RestClientDriver;
import dnt.websockets.integration.vertx.WebSocketServerDriver;
import io.vertx.core.Vertx;

import static dnt.websockets.vertx.VertxFactory.newVertx;

public abstract class AbstractIntegrationVertxTest
{
    private static final Vertx VERTX = newVertx();
    private static final WebSocketServerDriver serverDriver = new WebSocketServerDriver(VERTX);
    private static final WebSocketClientDriver clientDriver = new WebSocketClientDriver(VERTX, "source1");
    private static final RestClientDriver restDriver = new RestClientDriver(VERTX);

    protected ServerVertxDsl server = new ServerVertxDsl(serverDriver);
    protected ClientVertxDsl client = new ClientVertxDsl(clientDriver);
    protected RestVertxDsl rest = new RestVertxDsl(restDriver);
}
