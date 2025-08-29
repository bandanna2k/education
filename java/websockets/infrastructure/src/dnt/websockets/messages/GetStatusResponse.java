package dnt.websockets.messages;

import dnt.websockets.infrastructure.ExecutionLayer;

public class GetStatusResponse extends AbstractResponse
{
    public String status;

    public GetStatusResponse()
    {
        super();
    }
    public GetStatusResponse(long correlationId, String status)
    {
        super(correlationId);
        this.status = status;
    }

    @Override
    public void visit(ExecutionLayer executionLayer, MessageVisitor visitor)
    {
        visitor.visit(executionLayer, this);
    }

    @Override
    public String toString() {
        return "GetStatusResponse{" +
                "status='" + status + '\'' +
                "} " + super.toString();
    }
}
