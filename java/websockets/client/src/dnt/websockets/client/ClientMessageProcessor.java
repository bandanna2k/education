package dnt.websockets.client;

import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.GetStatusRequest;
import dnt.websockets.communications.GetStatusResponse;
import dnt.websockets.communications.MessageVisitor;

public class ClientMessageProcessor implements MessageVisitor
{
    @Override
    public void visit(ExecutionLayer executionLayer, GetStatusRequest request)
    {
        executionLayer.clientResponseToRequest(new GetStatusResponse("Wicked"));
    }
}
