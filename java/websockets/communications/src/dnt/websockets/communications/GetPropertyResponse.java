package dnt.websockets.communications;

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
    public void visit(ResponseVisitor visitor)
    {
        visitor.visit(this);
    }

    @Override
    public String toString()
    {
        return "GetPropertyResponse{" +
                "value='" + value + '\'' +
                "} " + super.toString();
    }
}
