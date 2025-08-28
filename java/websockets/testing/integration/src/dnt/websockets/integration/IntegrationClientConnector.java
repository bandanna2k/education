package dnt.websockets.integration;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.MessageVisitor;
import dnt.websockets.server.ServerTextMessageHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IntegrationClientConnector
{
    private final List<ServerTextMessageHandler> textMessageHandlers = new ArrayList<>();

    public IntegrationClientConnector(final ExecutionLayer executionLayer, MessageVisitor messageVisitor)
    {
        this.textMessageHandlers.add(new ServerTextMessageHandler(executionLayer, messageVisitor));
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
}
