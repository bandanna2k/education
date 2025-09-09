package dnt.websockets.integration.base;

import dnt.websockets.client.ClientMessageProcessor;
import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.integration.ClientDriver;
import dnt.websockets.integration.dsl.ClientDsl;
import dnt.websockets.integration.dsl.IntegrationDsl;
import dnt.websockets.integration.dsl.ServerDsl;
import dnt.websockets.integration.infrastructure.IntegrationExecutionLayer;
import dnt.websockets.integration.MessageCollector;
import dnt.websockets.integration.ServerDriver;
import dnt.websockets.messages.GetPropertyResponse;
import dnt.websockets.messages.SetPropertyRequest;
import dnt.websockets.server.ServerMessageProcessor;
import dnt.websockets.server.ServerTextMessageHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class AbstractIntegrationTest
{
    static
    {
        System.setProperty("org.slf4j.simpleLogger.log.dnt.websockets", "DEBUG");
    }
    private final ServerMessageProcessor serverMessageProcessor = new ServerMessageProcessor();
    private final ClientMessageProcessor clientMessageProcessor = new ClientMessageProcessor();
    private final ClientMessageProcessor clientMessageProcessor2 = new ClientMessageProcessor();

    private final MessageCollector testServerMessageCollector = new MessageCollector("Abstract Integration Test Server", serverMessageProcessor);
    private final MessageCollector testClientMessageCollector = new MessageCollector("Abstract Integration Test Client", clientMessageProcessor);
    private final MessageCollector testClientMessageCollector2 = new MessageCollector("Abstract Integration Test Client 2", clientMessageProcessor2);

    private final IntegrationExecutionLayer executionLayer = new IntegrationExecutionLayer(
            testServerMessageCollector,
            new MessageCollector("Client Test Collector (Multi)",
                    testClientMessageCollector,
                    testClientMessageCollector2));

    protected final ServerDriver serverDriver = new ServerDriver(executionLayer, serverMessageProcessor);
    protected final ClientDriver clientDriver = new ClientDriver(executionLayer, clientMessageProcessor);

    protected final ServerDsl server = new ServerDsl(serverDriver, testServerMessageCollector);
    protected final ClientDsl client = new ClientDsl(clientDriver, testClientMessageCollector);
    protected final IntegrationDsl integration = new IntegrationDsl(executionLayer);

    protected final ClientDriver clientDriver2 = new ClientDriver(executionLayer, clientMessageProcessor2);
    protected final ClientDsl client2 = new ClientDsl(clientDriver2, testClientMessageCollector2);

    private final Map<String, ClientDsl> clients = Map.of("session1", client, "session2", client2);

    protected ClientDsl client(String session)
    {
        ClientDsl clientDsl = clients.get(session);
        assert clientDsl != null : "No client found for session: " + session;
        return clientDsl;
    }

    @BeforeAll
    public static void warmUpObjectMappers() throws Exception
    {
        ClientTextMessageHandler.OBJECT_MAPPER.writeValueAsBytes(new GetPropertyResponse(1, "key"));
        ServerTextMessageHandler.OBJECT_MAPPER.writeValueAsBytes(new SetPropertyRequest("key", "value"));
    }

    @BeforeEach
    public void setUp()
    {
        executionLayer.register("session1", new ClientTextMessageHandler(executionLayer, testClientMessageCollector));
        executionLayer.register("session2", new ClientTextMessageHandler(executionLayer, testClientMessageCollector2));
    }

    @AfterEach
    public void tearDown()
    {
        boolean complete = integration.isComplete();
        if(!complete)
        {
            integration.resumeProcessing();
            fail("Deferred futures exist");
        }
    }
}
