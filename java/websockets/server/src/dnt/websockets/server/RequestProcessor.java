package dnt.websockets.server;

import dnt.websockets.communications.MessagePublisher;
import dnt.websockets.communications.OptionsRequest;
import dnt.websockets.communications.OptionsResponse;
import dnt.websockets.communications.RequestVisitor;

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
