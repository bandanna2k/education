package dnt.websockets.messages;

import dnt.websockets.infrastructure.ExecutionLayer;

public class ServerPushMessage extends AbstractMessage
{
    @Override
    public void visit(ExecutionLayer executionLayer, MessageVisitor visitor)
    {
        visitor.visit(executionLayer, this);
    }
}
