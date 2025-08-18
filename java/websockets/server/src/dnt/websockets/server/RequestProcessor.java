package dnt.websockets.server;

import dnt.websockets.communications.*;

public class RequestProcessor implements RequestVisitor
{
    private final Publisher publisher;

    public RequestProcessor(Publisher publisher)
    {
        this.publisher = publisher;
    }

    @Override
    public void visit(OptionsRequest optionsRequest)
    {
        publisher.send(new OptionsResponse(optionsRequest.correlationId));
    }
}
