package dnt.websockets.messages;

import dnt.websockets.infrastructure.ExecutionLayer;

public abstract class AbstractRequest extends AbstractMessage
{
    public static final long NO_CORRELATION_ID = -1;

    public long correlationId = NO_CORRELATION_ID;

    public abstract void visit(ExecutionLayer executionLayer, MessageVisitor visitor);

    @Override
    public String toString() {
        return "AbstractRequest{" +
                "correlationId=" + correlationId +
                "} " + super.toString();
    }

    public <T extends AbstractRequest> T attachCorrelationId(long correlationId)
    {
        this.correlationId = correlationId;
        return (T)this;
    }
}
