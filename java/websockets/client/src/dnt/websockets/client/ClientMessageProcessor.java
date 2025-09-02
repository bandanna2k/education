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
        if("fail_requests".equalsIgnoreCase(status))
        {
            ErrorResponse response = new ErrorResponse(request.correlationId, 503, "Request not accepted at this time.");
            executionLayer.clientCompleteResponse(response);
            return;
        }
        GetStatusResponse response = new GetStatusResponse(request.correlationId, status);
        executionLayer.clientCompleteResponse(response);
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
