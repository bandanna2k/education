package dnt.websockets.integration.dsl;

import dnt.websockets.client.ClientMessageProcessor;
import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.communications.*;
import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.integration.MessageCollector;
import dnt.websockets.server.ServerMessageProcessor;
import dnt.websockets.server.ServerTextMessageHandler;
import org.junit.After;
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

    protected final ServerDsl server = new ServerDsl(executionLayer, serverMessageProcessor);
    protected final ClientDsl client = new ClientDsl(executionLayer, clientMessageCollector);
    protected final IntegrationDsl integration = new IntegrationDsl(executionLayer);

    protected final ClientDsl client2 = new ClientDsl(executionLayer, clientMessageCollector2);

    private final Map<String, ClientDsl> clients = Map.of("session1", client, "session2", client2);

    protected ClientDsl client(String session)
    {
        return clients.get(session);
    }

    @BeforeClass
    public static void warmUpObjectMappers() throws Exception
    {
        ClientTextMessageHandler.OBJECT_MAPPER.writeValueAsBytes(new GetPropertyResponse(1, "key"));
        ServerTextMessageHandler.OBJECT_MAPPER.writeValueAsBytes(new SetPropertyRequest("key", "value"));
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
