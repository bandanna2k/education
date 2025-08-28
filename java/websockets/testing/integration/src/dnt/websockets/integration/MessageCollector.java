package dnt.websockets.integration;

import dnt.websockets.communications.*;

import java.util.LinkedList;
import java.util.Queue;

public class MessageCollector implements MessageVisitor
{
    private final String name;
    private final Queue<AbstractMessage> messages = new LinkedList<>();
    private final MessageVisitor messageVisitor;

    public MessageCollector(String name, MessageVisitor messageVisitor)
    {
        this.name = name;
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
    public void visit(ExecutionLayer executionLayer, ClientPushPrice message)
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
    public void visit(ExecutionLayer executionLayer, ServerPushMessage message)
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
        return "MessageCollector2{" +
                "name='" + name + '\'' +
                ", messages=" + messages +
                ", messageVisitor=" + messageVisitor +
                '}';
    }
}
