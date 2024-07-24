package education.jackson.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import education.jackson.requests.Deposit;
import education.jackson.requests.RequestVisitor;
import education.jackson.requests.Withdrawal;

import java.util.Locale;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Balance.class, name = "balance"),
        @JsonSubTypes.Type(value = Error.class, name = "error")
})
public abstract class Response
{
    public UUID uuid;
    public String type;

    public Response()
    {
    }
    public Response(UUID uuid)
    {
        this.uuid = uuid;
        this.type = this.getClass().getSimpleName().toLowerCase(Locale.ROOT);
    }

    @Override
    public String toString()
    {
        return "Request{" +
                "uuid=" + uuid +
                ", type='" + type + '\'' +
                '}';
    }
}
