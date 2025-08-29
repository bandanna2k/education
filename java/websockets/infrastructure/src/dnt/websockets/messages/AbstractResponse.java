package dnt.websockets.messages;

import dnt.websockets.infrastructure.ExecutionLayer;

public abstract class AbstractResponse extends AbstractMessage
{
    public static final long NO_CORRELATION_ID = -1;

    public long correlationId = NO_CORRELATION_ID;

    public AbstractResponse()
    {
        super();
    }

    public AbstractResponse(long correlationId)
    {
        this();
        this.correlationId = correlationId;
    }

    public abstract void visit(ExecutionLayer executionLayer, MessageVisitor visitor);

    @Override
    public String toString()
    {
        return "AbstractResponse{" +
                "correlationId=" + correlationId +
                "} " + super.toString();
    }
}
