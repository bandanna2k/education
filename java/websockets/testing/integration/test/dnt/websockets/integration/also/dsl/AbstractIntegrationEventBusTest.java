package dnt.websockets.integration.also.dsl;

import dnt.websockets.integration.also.EventBusClientDriver;
import dnt.websockets.integration.also.EventBusServerDriver;
import io.vertx.core.Vertx;

import static dnt.websockets.vertx.VertxFactory.newVertx;

public abstract class AbstractIntegrationEventBusTest
{
    private static final Vertx VERTX = newVertx();
    private static final EventBusServerDriver serverDriver = new EventBusServerDriver(VERTX);
    private static final EventBusClientDriver clientDriver = new EventBusClientDriver(VERTX);

    protected EventBusServerDsl server = new EventBusServerDsl(serverDriver);
    protected EventBusClientDsl client = new EventBusClientDsl(clientDriver);
}
