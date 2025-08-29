package dnt.websockets.messages;

import dnt.websockets.infrastructure.ExecutionLayer;

public abstract class AbstractResponse extends AbstractMessage
{
    public static final long NO_CORRELATION_ID = -1;

    public long correlationId = NO_CORRELATION_ID;
    public String type;

    public AbstractResponse()
    {
        super();
    }

    public AbstractResponse(long correlationId)
    {
        this.correlationId = correlationId;
        this.type = this.getClass().getSimpleName();
    }

    public abstract void visit(ExecutionLayer executionLayer, MessageVisitor visitor);

    @Override
    public String toString()
    {
        return "AbstractResponse{" +
                "correlationId=" + correlationId +
                ", type='" + type + '\'' +
                "} " + super.toString();
    }
}
