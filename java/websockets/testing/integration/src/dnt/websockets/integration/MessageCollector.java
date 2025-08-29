package dnt.websockets.integration;

import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.messages.*;

import java.util.LinkedList;
import java.util.Queue;

public class MessageCollector implements MessageVisitor
{
    // TODO assert all values

    private final String name;
    private final Queue<AbstractMessage> messages = new LinkedList<>();
    private final MessageVisitor messageVisitor;

    public MessageCollector(String nameHelpsWithDebugging, MessageVisitor messageVisitor)
    {
        this.name = nameHelpsWithDebugging;
        this.messageVisitor = messageVisitor;
    }

    @Override
    public void visit(ExecutionLayer executionLayer, AbstractMessage message)
    {
        this.messageVisitor.visit(executionLayer, message);
        messages.add(message);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, GetPropertyRequest request)
    {
        this.messageVisitor.visit(executionLayer, request);
        messages.add(request);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, SetPropertyRequest request)
    {
        this.messageVisitor.visit(executionLayer, request);
        messages.add(request);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, ClientPushPulse message)
    {
        this.messageVisitor.visit(executionLayer, message);
        messages.add(message);
    }


    @Override
    public void visit(ExecutionLayer executionLayer, ErrorResponse response)
    {
        this.messageVisitor.visit(executionLayer, response);
        messages.add(response);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, GetPropertyResponse response)
    {
        this.messageVisitor.visit(executionLayer, response);
        messages.add(response);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, SetPropertyResponse response)
    {
        this.messageVisitor.visit(executionLayer, response);
        messages.add(response);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, ServerPushMessage message)
    {
        this.messageVisitor.visit(executionLayer, message);
        messages.add(message);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, GetStatusRequest request)
    {
        this.messageVisitor.visit(executionLayer, request);
        messages.add(request);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, GetStatusResponse message)
    {
        this.messageVisitor.visit(executionLayer, message);
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
                ", messageVisitor=" + messageVisitor +
                '}';
    }
}
