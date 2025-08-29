package dnt.websockets.integration.infrastructure;

import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.MessageVisitor;
import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.server.ServerTextMessageHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// TODO Can I get rid of this?
public class IntegrationServerConnector
{
    private final List<ServerTextMessageHandler> textMessageHandlers = new ArrayList<>();

    public IntegrationServerConnector(final ExecutionLayer executionLayer, MessageVisitor messageVisitor)
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
