package dnt.websockets.integration;

import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.messages.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MessageCollector implements MessageVisitor
{
    // TODO assert all values

    private final String name;
    private final Queue<AbstractMessage> messages = new LinkedList<>();
    private final List<MessageVisitor> messageVisitors;

    public MessageCollector(String nameHelpsWithDebugging, MessageVisitor... messageVisitors)
    {
        this.name = nameHelpsWithDebugging;
        this.messageVisitors = List.of(messageVisitors);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, GetPropertyRequest request)
    {
        messageVisitors.forEach(v -> v.visit(executionLayer, request));
        messages.add(request);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, SetPropertyRequest request)
    {
        messageVisitors.forEach(v -> v.visit(executionLayer, request));
        messages.add(request);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, ClientPushPulse message)
    {
        messageVisitors.forEach(v -> v.visit(executionLayer, message));
        messages.add(message);
    }


    @Override
    public void visit(ExecutionLayer executionLayer, ErrorResponse response)
    {
        messageVisitors.forEach(v -> v.visit(executionLayer, response));
        messages.add(response);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, GetPropertyResponse response)
    {
        messageVisitors.forEach(v -> v.visit(executionLayer, response));
        messages.add(response);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, SetPropertyResponse response)
    {
        messageVisitors.forEach(v -> v.visit(executionLayer, response));
        messages.add(response);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, ServerPushMessage message)
    {
        messageVisitors.forEach(v -> v.visit(executionLayer, message));
        messages.add(message);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, GetStatusRequest request)
    {
        messageVisitors.forEach(v -> v.visit(executionLayer, request));
        messages.add(request);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, GetStatusResponse message)
    {
        messageVisitors.forEach(v -> v.visit(executionLayer, message));
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

    @Override
    public String toString()
    {
        return "MessageCollector{" +
                "name='" + name + '\'' +
                ", messages=" + messages +
                ", messageVisitors=" + messageVisitors +
                '}';
    }

    public void clear()
    {
        this.messages.clear();
    }
}
