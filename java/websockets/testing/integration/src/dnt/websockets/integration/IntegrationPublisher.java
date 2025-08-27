package dnt.websockets.integration;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.MessageVisitor;
import dnt.websockets.communications.Publisher;

class IntegrationPublisher implements Publisher
{
    private final ExecutionLayer executionLayer;
    private final MessageVisitor messageVisitor;

    IntegrationPublisher(ExecutionLayer executionLayer, MessageVisitor messageVisitor)
    {
        this.executionLayer = executionLayer;
        this.messageVisitor = messageVisitor;
    }

    @Override
    public void send(AbstractMessage message)
    {
        message.visit(executionLayer, messageVisitor);
    }
}
