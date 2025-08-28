package dnt.websockets.integration.dsl;

import dnt.websockets.client.ClientMessageProcessor;
import dnt.websockets.communications.*;
import dnt.websockets.server.ServerMessageProcessor;

import java.util.LinkedList;
import java.util.Queue;

public class TestClientMessageCollector extends ClientMessageProcessor
{
    private final Queue<AbstractMessage> messages = new LinkedList<>();

    @Override
    public void visit(ExecutionLayer executionLayer, AbstractMessage message)
    {
        super.visit(executionLayer, message);
        messages.add(message);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, ErrorResponse response)
    {
        super.visit(executionLayer, response);
        messages.add(response);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, GetStatusRequest request)
    {
        super.visit(executionLayer, request);
        messages.add(request);
    }

    public <T extends AbstractMessage> T getLastMessage()
    {
        if(messages.isEmpty())
        {
            return null;
        }
        return (T) messages.remove();
    }
}
