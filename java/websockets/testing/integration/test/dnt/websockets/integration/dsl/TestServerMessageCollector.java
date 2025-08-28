package dnt.websockets.integration.dsl;

import dnt.websockets.communications.*;
import dnt.websockets.server.ServerMessageProcessor;

import java.util.LinkedList;
import java.util.Queue;

public class TestServerMessageCollector extends ServerMessageProcessor
{
    private final Queue<AbstractMessage> messages = new LinkedList<>();

    @Override
    public void visit(ExecutionLayer executionLayer, AbstractMessage message)
    {
        super.visit(executionLayer, message);
        messages.add(message);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, GetPropertyRequest request)
    {
        super.visit(executionLayer, request);
        messages.add(request);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, SetPropertyRequest request)
    {
        super.visit(executionLayer, request);
        messages.add(request);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, ClientPushPulse message)
    {
        super.visit(executionLayer, message);
        messages.add(message);
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
