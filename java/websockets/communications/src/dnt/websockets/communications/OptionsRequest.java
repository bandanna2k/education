package dnt.websockets.communications;

public class OptionsRequest extends AbstractRequest
{
    public OptionsRequest() {}
    public OptionsRequest(long correlationId)
    {
        super(correlationId);
    }

    @Override
    public void visit(RequestVisitor visitor)
    {
        visitor.visit(this);
    }

    @Override
    public String toString()
    {
        return "OptionsRequest{} " + super.toString();
    }
}
