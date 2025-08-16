package dnt.websockets.server;

import dnt.websockets.server.infrastructure.MessagePublisher;
import dnt.websockets.server.infrastructure.OptionsRequest;
import dnt.websockets.server.infrastructure.OptionsResponse;
import dnt.websockets.server.infrastructure.RequestVisitor;

public class RequestProcessor implements RequestVisitor
{
    private final MessagePublisher messagePublisher;

    public RequestProcessor(MessagePublisher messagePublisher)
    {
        this.messagePublisher = messagePublisher;
    }

    @Override
    public void visit(OptionsRequest optionsRequest)
    {
        messagePublisher.send(new OptionsResponse(optionsRequest.correlationId));
    }
}
