package dnt.websockets.communications;

import java.util.Optional;

public class SetPropertyRequest extends AbstractRequest
{
    public String key;
    public String value;

    public SetPropertyRequest()
    {
        super();
    }
    public SetPropertyRequest(String key, String value)
    {
        this();
        this.key = key;
        this.value = value;
    }

    @Override
    public void visit(ExecutionLayer executionLayer, RequestVisitor visitor)
    {
        visitor.visit(executionLayer, this);
    }

    public Optional<ErrorResponse> validate()
    {
        if(key == null || key.isEmpty())
            return Optional.of(new ErrorResponse(correlationId, 400, "Key cannot be empty."));
        if(value == null || value.isEmpty())
            return Optional.of(new ErrorResponse(correlationId, 400, "Value cannot be empty."));
        return Optional.empty();
    }

    @Override
    public String toString()
    {
        return "SetPropertyRequest{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                "} " + super.toString();
    }
}
