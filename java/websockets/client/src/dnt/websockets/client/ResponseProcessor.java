package dnt.websockets.client;

import dnt.websockets.communications.*;

// TODO Is there a point to this class. The future is the response handler
public class ResponseProcessor implements ResponseVisitor
{
    private final ExecutionLayer executionLayer;

    public ResponseProcessor(ExecutionLayer executionLayer)
    {
        this.executionLayer = executionLayer;
    }

    @Override
    public void visit(GetPropertyResponse response)
    {
        executionLayer.respond(response);
    }

    @Override
    public void visit(SetPropertyResponse response)
    {
        executionLayer.respond(response);
    }
}
