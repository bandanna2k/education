package dnt.websockets.messages;

import dnt.websockets.infrastructure.ExecutionLayer;

public class GetStatusRequest extends AbstractServerRequest
{
    public GetStatusRequest() {}
    public GetStatusRequest(String clientId)
    {
        super(clientId);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, MessageVisitor visitor)
    {
        visitor.visit(executionLayer, this);
    }

    @Override
    public String toString() {
        return "GetClientStatus{" +
                "client='" + clientId + '\'' +
                "} " + super.toString();
    }
}
