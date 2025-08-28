package dnt.websockets.communications;

public class SetPropertyResponse extends AbstractResponse
{
    public SetPropertyResponse() {}
    public SetPropertyResponse(long correlationId)
    {
        super(correlationId);
    }

    @Override
    public void visit(ResponseVisitor visitor)
    {
        visitor.visit(this);
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}
