package dnt.websockets.client;

import dnt.websockets.communications.*;

public class ClientMessageProcessor implements MessageVisitor
{
    @Override
    public void visit(ExecutionLayer executionLayer, GetStatusRequest request)
    {
        executionLayer.clientResponseToRequest(new GetStatusResponse("Wicked"));
    }

    @Override
    public void visit(ExecutionLayer executionLayer, ServerPushMessage message)
    {
        System.out.println("Client received message. " + message);
    }
}
