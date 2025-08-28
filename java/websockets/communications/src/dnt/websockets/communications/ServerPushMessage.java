package dnt.websockets.communications;

public class ServerPushMessage extends AbstractMessage
{
    @Override
    public void visit(ExecutionLayer executionLayer, MessageVisitor visitor)
    {
        visitor.visit(executionLayer, this);
    }
}
