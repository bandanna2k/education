package dnt.websockets.integration.vertx.dsl;

import dnt.websockets.integration.vertx.RestClientDriver;
import dnt.websockets.integration.vertx.WebSocketClientDriver;
import dnt.websockets.integration.vertx.WebSocketServerDriver;
import io.vertx.core.Vertx;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static dnt.websockets.vertx.VertxFactory.newVertx;
import static org.junit.Assert.fail;

public abstract class AbstractIntegrationVertxTest
{
    static
    {
        System.setProperty("org.slf4j.simpleLogger.log.dnt.websockets", "DEBUG");
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIntegrationVertxTest.class);

    private static final Vertx VERTX = newVertx();
    private static final WebSocketServerDriver serverDriver = new WebSocketServerDriver(VERTX);

    private final WebSocketClientDriver clientDriver = new WebSocketClientDriver(VERTX, "source1");
    private final WebSocketClientDriver clientDriver2 = new WebSocketClientDriver(VERTX, "source2");
    private final RestClientDriver restDriver = new RestClientDriver(VERTX);

    protected static ServerWebSocketDsl server = new ServerWebSocketDsl(serverDriver);
    protected ClientWebSocketDsl client = new ClientWebSocketDsl(clientDriver);
    protected ClientWebSocketDsl client2 = new ClientWebSocketDsl(clientDriver2);
    protected RestVertxDsl rest = new RestVertxDsl(restDriver);

    private final Map<String, ClientWebSocketDsl> clients = Map.of(
            clientDriver.getClientId(), client,
            clientDriver2.getClientId(), client2
    );

    protected ClientWebSocketDsl client(String sessionId)
    {
        return clients.get(sessionId);
    }

    @BeforeClass
    public static void beforeClass()
    {
        serverDriver.start()
                .onFailure(throwable -> fail("WebSocket server failed to start. " + throwable.getMessage()))
                .onSuccess(webSocket -> LOGGER.info("WebSocket server started"))
                .toCompletionStage().toCompletableFuture().join();
    }

    @Before
    public void setUp()
    {
        clientDriver.start()
                .onFailure(throwable -> fail("WebSocket client failed to start. " + throwable.getMessage()))
                .onSuccess(webSocket -> LOGGER.info("WebSocket client started"))
                .toCompletionStage().toCompletableFuture().join();
        clientDriver2.start()
                .onFailure(throwable -> fail("WebSocket client failed to start. " + throwable.getMessage()))
                .onSuccess(webSocket -> LOGGER.info("WebSocket client started"))
                .toCompletionStage().toCompletableFuture().join();
//        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }
}
