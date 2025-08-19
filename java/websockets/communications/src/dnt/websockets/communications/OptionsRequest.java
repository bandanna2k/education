package dnt.websockets.communications;

public class OptionsRequest extends AbstractRequest
{
    public OptionsRequest()
    {
        super();
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
