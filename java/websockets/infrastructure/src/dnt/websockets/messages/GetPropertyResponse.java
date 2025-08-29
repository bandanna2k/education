package dnt.websockets.messages;

import dnt.websockets.infrastructure.ExecutionLayer;

public class GetPropertyResponse extends AbstractResponse
{
    public String value;

    public GetPropertyResponse() {}
    public GetPropertyResponse(long correlationId, String value)
    {
        super(correlationId);
        this.value = value;
    }

    @Override
    public void visit(ExecutionLayer executionLayer, MessageVisitor visitor)
    {
        visitor.visit(executionLayer, this);
    }

    @Override
    public String toString()
    {
        return "GetPropertyResponse{" +
                "value='" + value + '\'' +
                "} " + super.toString();
    }
}
