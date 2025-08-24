package dnt.websockets.integration;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.Publisher;
import dnt.websockets.communications.PushMessageVisitor;

class IntegrationPublisher implements Publisher
{
    private final PushMessageVisitor pushMessageVisitor;

    IntegrationPublisher(PushMessageVisitor pushMessageVisitor)
    {
        this.pushMessageVisitor = pushMessageVisitor;
    }

    @Override
    public void send(AbstractMessage message)
    {
        message.visit(pushMessageVisitor);
    }
}
