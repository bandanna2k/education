package dnt.websockets.communications;

public abstract class AbstractRequest extends AbstractMessage
{
    public static final long NO_CORRELATION_ID = -1;

    public long correlationId = NO_CORRELATION_ID;

    public abstract void visit(RequestVisitor visitor);

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
