package dnt.websockets.communications;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OptionsRequest.class, name = "OptionsRequest"),
})
public abstract class AbstractRequest extends AbstractMessage
{
    public static final long NO_CORRELATION_ID = -1;

    public long correlationId = NO_CORRELATION_ID;
    public String type;

    public AbstractRequest()
    {
        this.type = this.getClass().getSimpleName();
    }
    public AbstractRequest(long correlationId)
    {
        this();
        this.correlationId = correlationId;
    }

    public abstract void visit(RequestVisitor visitor);

    @Override
    public String toString()
    {
        return "AbstractRequest{" +
                "correlationId=" + correlationId +
                ", type='" + type + '\'' +
                "} " + super.toString();
    }
}
