package dnt.websockets.integration;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.Publisher;

import java.util.LinkedList;
import java.util.Queue;

class IntegrationPublisher implements Publisher
{
    Queue<AbstractMessage> requests = new LinkedList<>();

    @Override
    public void send(AbstractMessage message)
    {
        requests.add(message);
    }

    @Override
    public long getNextCorrelationId()
    {
        return -1;
    }

    public <T extends AbstractMessage> T getLastMessage()
    {
        return (T) requests.remove();
    }
}
