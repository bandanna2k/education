package dnt.websockets.messages;

import dnt.websockets.infrastructure.ExecutionLayer;

public class SetPropertyResponse extends AbstractResponse
{
    public SetPropertyResponse() {}
    public SetPropertyResponse(long correlationId)
    {
        super(correlationId);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, MessageVisitor visitor)
    {
        visitor.visit(executionLayer, this);
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}
