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
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.Map;

import static org.junit.Assert.fail;

public abstract class AbstractIntegrationTest
{
    private final ServerMessageProcessor serverMessageProcessor = new ServerMessageProcessor();
    private final ClientMessageProcessor clientMessageProcessor = new ClientMessageProcessor();
    private final ClientMessageProcessor clientMessageProcessor2 = new ClientMessageProcessor();

    private final MessageCollector serverMessageCollector = new MessageCollector("Abstract Integration Test Server", serverMessageProcessor);
    private final MessageCollector clientMessageCollector = new MessageCollector("Abstract Integration Test Client", clientMessageProcessor);
    private final MessageCollector clientMessageCollector2 = new MessageCollector("Abstract Integration Test Client", clientMessageProcessor2);

    private final IntegrationExecutionLayer executionLayer = new IntegrationExecutionLayer(serverMessageCollector, clientMessageCollector);

    protected final ServerDriver serverDriver = new ServerDriver(executionLayer, serverMessageProcessor);
    protected final ClientDriver clientDriver = new ClientDriver(executionLayer, clientMessageProcessor);

    protected final ServerDsl server = new ServerDsl(serverDriver, serverMessageCollector);
    protected final ClientDsl client = new ClientDsl(clientDriver, clientMessageCollector);
    protected final IntegrationDsl integration = new IntegrationDsl(executionLayer);

    protected final ClientDriver clientDriver2 = new ClientDriver(executionLayer, clientMessageProcessor2);
    protected final ClientDsl client2 = new ClientDsl(clientDriver2, clientMessageCollector2);

    private final Map<String, ClientDsl> clients = Map.of("session1", client, "session2", client2);

    protected ClientDsl client(String session)
    {
        ClientDsl clientDsl = clients.get(session);
        assert clientDsl != null : "No client found for session: " + session;
        return clientDsl;
    }

    @BeforeClass
    public static void warmUpObjectMappers() throws Exception
    {
        ClientTextMessageHandler.OBJECT_MAPPER.writeValueAsBytes(new GetPropertyResponse(1, "key"));
        ServerTextMessageHandler.OBJECT_MAPPER.writeValueAsBytes(new SetPropertyRequest("key", "value"));
    }

    @Before
    public void setUp()
    {
        executionLayer.register("session1", new ClientTextMessageHandler(executionLayer, clientMessageCollector));
        executionLayer.register("session2", new ClientTextMessageHandler(executionLayer, clientMessageCollector2));
    }

    @After
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
