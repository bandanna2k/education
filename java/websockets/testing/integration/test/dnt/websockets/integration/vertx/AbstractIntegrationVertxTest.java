package dnt.websockets.integration.vertx;

import dnt.websockets.integration.vertx.dsl.ClientVertxDriver;
import dnt.websockets.integration.vertx.dsl.ClientVertxDsl;
import dnt.websockets.integration.vertx.dsl.ServerVertxDriver;
import dnt.websockets.integration.vertx.dsl.ServerVertxDsl;

public abstract class AbstractIntegrationVertxTest
{
    private static final ServerVertxDriver serverDriver = new ServerVertxDriver();
    private static final ClientVertxDriver clientDriver = new ClientVertxDriver();

    protected ServerVertxDsl server = new ServerVertxDsl(serverDriver);
    protected ClientVertxDsl client = new ClientVertxDsl(clientDriver);
}
