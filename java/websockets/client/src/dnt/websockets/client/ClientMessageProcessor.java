package dnt.websockets.client;

import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.messages.*;

public class ClientMessageProcessor implements MessageVisitor
{
    private String status = "Wicked";

    @Override
    public void visit(ExecutionLayer executionLayer, GetStatusRequest request)
    {
        if("do_not_send_response".equalsIgnoreCase(status))
        {
            return;
        }
        executionLayer.clientResponseToRequest(new GetStatusResponse(status));
    }

    @Override
    public void visit(ExecutionLayer executionLayer, ServerPushMessage message)
    {
        System.out.println("Client received push message. " + message);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, SetPropertyResponse response)
    {
        System.out.println("Set property succeeded." + response);
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
