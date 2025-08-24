package dnt.websockets.integration;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.server.RequestProcessor;
import dnt.websockets.server.ServerTextMessageHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IntegrationServer
{
    private final List<ServerTextMessageHandler> textMessageHandlers = new ArrayList<>();
    private final RequestProcessor requestProcessor;

    public IntegrationServer(final ExecutionLayer executionLayer, RequestProcessor requestProcessor)
    {
        this.requestProcessor = requestProcessor;
        this.textMessageHandlers.add(new ServerTextMessageHandler(executionLayer, this.requestProcessor));
    }

    public void push(AbstractMessage message)
    {
        Iterator<ServerTextMessageHandler> iterator = textMessageHandlers.iterator();
        while (iterator.hasNext())
        {
            ServerTextMessageHandler next;
            try
            {
                next = iterator.next();
                next.send(message);
            }
            catch (Exception e)
            {
                iterator.remove();
            }
        }
    }

    public String getProperty(String key)
    {
        return requestProcessor.get(key);
    }
}
