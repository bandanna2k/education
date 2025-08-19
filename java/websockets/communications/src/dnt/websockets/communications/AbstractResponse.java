package dnt.websockets.communications;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
public abstract class AbstractResponse extends AbstractMessage
{
    public static final long NO_CORRELATION_ID = -1;

    public long correlationId = NO_CORRELATION_ID;
    public String type;

    public AbstractResponse()
    {
    }

    public AbstractResponse(long correlationId)
    {
        this.correlationId = correlationId;
        this.type = this.getClass().getSimpleName();
    }

    public abstract void visit(ResponseVisitor visitor);

    @Override
    public String toString()
    {
        return "AbstractResponse{" +
                "correlationId=" + correlationId +
                ", type='" + type + '\'' +
                "} " + super.toString();
    }
}
