package dnt.websockets.communications;

public class PushMessage extends AbstractMessage
{
    @Override
    public void visit(PushMessageVisitor visitor)
    {
        visitor.visit(this);
    }
}
