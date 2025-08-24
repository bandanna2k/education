package dnt.websockets.integration.vertx.dsl;

import dnt.websockets.integration.vertx.VertxClientDriver;
import dnt.websockets.integration.vertx.VertxRestDriver;
import dnt.websockets.integration.vertx.VertxServerDriver;
import io.vertx.core.Vertx;

import static dnt.websockets.vertx.VertxFactory.newVertx;

public abstract class AbstractIntegrationVertxTest
{
    private static final Vertx VERTX = newVertx();
    private static final VertxServerDriver serverDriver = new VertxServerDriver(VERTX);
    private static final VertxClientDriver clientDriver = new VertxClientDriver(VERTX, "source1");
    private static final VertxRestDriver restDriver = new VertxRestDriver(VERTX);

    protected ServerVertxDsl server = new ServerVertxDsl(serverDriver);
    protected ClientVertxDsl client = new ClientVertxDsl(clientDriver);
    protected RestVertxDsl rest = new RestVertxDsl(restDriver);
}
