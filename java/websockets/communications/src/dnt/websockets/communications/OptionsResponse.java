package dnt.websockets.communications;

public class OptionsResponse extends AbstractResponse
{
    public OptionsResponse() {}
    public OptionsResponse(long correlationId)
    {
        super(correlationId);
    }

    @Override
    public void visit(ResponseVisitor visitor)
    {
        visitor.visit(this);
    }
}
