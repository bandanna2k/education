package dnt.websockets.client;

import dnt.websockets.communications.*;

public class ResponseProcessor implements ResponseVisitor
{
    private final WebSocketExecutorLayer executor;

    public ResponseProcessor(WebSocketExecutorLayer executor)
    {
        this.executor = executor;
    }

    @Override
    public void visit(OptionsResponse optionsResponse)
    {
        executor.onResponseReceived(optionsResponse.correlationId, optionsResponse);
    }
}
