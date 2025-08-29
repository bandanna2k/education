package dnt.websockets.messages;

import dnt.websockets.infrastructure.ExecutionLayer;

public class GetPropertyRequest extends AbstractRequest
{
    public String key;

    public GetPropertyRequest()
    {
        super();
    }
    public GetPropertyRequest(String key)
    {
        this();
        this.key = key;
    }

    @Override
    public void visit(ExecutionLayer executionLayer, MessageVisitor visitor)
    {
        visitor.visit(executionLayer, this);
    }

    @Override
    public String toString()
    {
        return "GetPropertyRequest{" +
                "key='" + key + '\'' +
                "} " + super.toString();
    }
}
