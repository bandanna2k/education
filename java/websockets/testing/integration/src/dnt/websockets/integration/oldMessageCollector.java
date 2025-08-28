package dnt.websockets.integration;

import dnt.websockets.communications.*;

import java.util.LinkedList;
import java.util.Queue;

class oldMessageCollector implements MessageVisitor
{
    private final Queue<AbstractMessage> messages = new LinkedList<>();

    @Override
    public void visit(ExecutionLayer executionLayer, AbstractMessage message)
    {
        messages.add(message);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, GetPropertyRequest request)
    {
        messages.add(request);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, SetPropertyRequest request)
    {
        messages.add(request);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, ClientPushPulse message)
    {
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
