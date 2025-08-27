package dnt.websockets.integration.dsl;

import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.GetPropertyResponse;
import dnt.websockets.communications.PushMessage;
import dnt.websockets.communications.SetPropertyRequest;
import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.integration.PushMessageCollector;
import dnt.websockets.server.RequestProcessor;
import dnt.websockets.server.ServerTextMessageHandler;
import org.junit.After;
import org.junit.BeforeClass;

import java.util.Map;

import static org.junit.Assert.fail;

public abstract class AbstractIntegrationTest
{
    private final PushMessageCollector clientMessageCollector = new PushMessageCollector();
    private final PushMessageCollector clientMessageCollector2 = new PushMessageCollector();

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

    private static class TestCollector extends PushMessageCollector
    {
        private final PushMessageCollector collector;
        private final PushMessageCollector collector2;

        private TestCollector(PushMessageCollector collector, PushMessageCollector collector2)
        {
            this.collector = collector;
            this.collector2 = collector2;
        }

        @Override
        public void visit(AbstractMessage message)
        {
            super.visit(message);
            collector.visit(message);
            collector2.visit(message);
        }
        @Override
        public void visit(PushMessage message)
        {
            super.visit(message);
            collector.visit(message);
            collector2.visit(message);
        }
    }
}
