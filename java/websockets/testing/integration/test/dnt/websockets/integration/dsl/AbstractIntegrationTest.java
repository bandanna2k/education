package dnt.websockets.integration.dsl;

import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.communications.GetPropertyRequest;
import dnt.websockets.communications.GetPropertyResponse;
import dnt.websockets.communications.SetPropertyRequest;
import dnt.websockets.communications.SetPropertyResponse;
import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.integration.PushMessageCollector;
import dnt.websockets.server.RequestProcessor;
import dnt.websockets.server.ServerTextMessageHandler;
import org.junit.After;
import org.junit.BeforeClass;

import static org.junit.Assert.fail;

public abstract class AbstractIntegrationTest
{
    private final PushMessageCollector collector = new PushMessageCollector();
    private final RequestProcessor requestProcessor = new RequestProcessor();
    private final IntegrationExecutionLayer executionLayer = new IntegrationExecutionLayer(requestProcessor, collector);

    protected final ServerDsl server = new ServerDsl(executionLayer, requestProcessor);
    protected final ClientDsl client = new ClientDsl(executionLayer, collector);
    protected final IntegrationDsl integration = new IntegrationDsl(executionLayer);

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        ClientTextMessageHandler.OBJECT_MAPPER.writeValueAsBytes(new SetPropertyResponse(1));
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
