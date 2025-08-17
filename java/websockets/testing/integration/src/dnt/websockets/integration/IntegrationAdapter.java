package dnt.websockets.integration;

import dnt.websockets.client.ResponseProcessor;
import dnt.websockets.communications.*;
import dnt.websockets.server.RequestProcessor;

class IntegrationAdapter
{
    private final RequestProcessor requestProcessor;
    private final ResponseProcessor responseProcessor;
    private final Publisher publisher = new Publisher()
    {
        long nextCorrelationId = 1;

        @Override
        public void send(AbstractMessage message)
        {
            IntegrationAdapter.this.send(message);
        }

        @Override
        public long getNextCorrelationId()
        {
            return nextCorrelationId++;
        }
    };

    IntegrationAdapter(RequestProcessorFactory requestProcessorFactory, ResponseProcessor responseProcessor)
    {
        this.requestProcessor = requestProcessorFactory.createInstance(publisher);
        this.responseProcessor = responseProcessor;
    }

    public void send(RequestFactory sender)
    {
        send(sender.createInstance(publisher.getNextCorrelationId()));
    }

    private void send(AbstractMessage message)
    {
        if (message instanceof OptionsRequest)
        {
            requestProcessor.visit((OptionsRequest) message);
        }

        if (message instanceof OptionsResponse)
        {
            responseProcessor.visit((OptionsResponse) message);
        }
    }

    interface RequestProcessorFactory
    {
        RequestProcessor createInstance(Publisher publisher);
    }

    interface RequestFactory
    {
        AbstractRequest createInstance(long correlationId);
    }
}
