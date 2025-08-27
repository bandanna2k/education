package dnt.websockets.integration.dsl;

import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.communications.*;
import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.integration.MessageCollector;
import dnt.websockets.server.RequestProcessor;
import dnt.websockets.server.ServerTextMessageHandler;
import org.junit.After;
import org.junit.BeforeClass;

import java.util.Map;

import static org.junit.Assert.fail;

public abstract class AbstractIntegrationTest
{
    private final MessageCollector clientMessageCollector = new MessageCollector();
    private final MessageCollector clientMessageCollector2 = new MessageCollector();

    private final RequestProcessor requestProcessor = new RequestProcessor();
    private final IntegrationExecutionLayer executionLayer = new IntegrationExecutionLayer(requestProcessor,
            new TestCollector(clientMessageCollector, clientMessageCollector2));

    protected final ServerDsl server = new ServerDsl(executionLayer, requestProcessor);
    protected final ClientDsl client = new ClientDsl(executionLayer, clientMessageCollector);
    protected final IntegrationDsl integration = new IntegrationDsl(executionLayer);

    protected final ClientDsl client2 = new ClientDsl(executionLayer, clientMessageCollector2);

    private final Map<String, ClientDsl> clients = Map.of("session1", client, "session2", client2);

    protected ClientDsl client(String session)
    {
        return clients.get(session);
    }

    @BeforeClass
    public static void beforeClass() throws Exception
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

    private static class TestCollector extends MessageCollector
    {
        private final MessageCollector collector;
        private final MessageCollector collector2;

        private TestCollector(MessageCollector collector, MessageCollector collector2)
        {
            this.collector = collector;
            this.collector2 = collector2;
        }

        @Override
        public void visit(ExecutionLayer executionLayer, AbstractMessage message)
        {
            super.visit(executionLayer, message);
            collector.visit(executionLayer, message);
            collector2.visit(executionLayer, message);
        }

        @Override
        public void visit(ExecutionLayer executionLayer, ServerPushMessage message)
        {
            super.visit(executionLayer, message);
            collector.visit(executionLayer, message);
            collector2.visit(executionLayer, message);
        }
    }
}
