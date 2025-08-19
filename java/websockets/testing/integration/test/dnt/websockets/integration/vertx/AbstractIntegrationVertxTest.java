package dnt.websockets.integration.vertx;

import dnt.websockets.integration.vertx.dsl.ClientVertxDsl;
import dnt.websockets.integration.vertx.dsl.ServerVertxDsl;

public abstract class AbstractIntegrationVertxTest
{
    protected ServerVertxDsl server = new ServerVertxDsl();
    protected ClientVertxDsl client = new ClientVertxDsl();
}
