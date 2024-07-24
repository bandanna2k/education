package education.jackson.requests;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Locale;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Deposit.class, name = "deposit"),
        @JsonSubTypes.Type(value = Withdrawal.class, name = "withdrawal")
})
public abstract class Request
{
    public UUID uuid;
    public String type;

    public Request()
    {
    }
    public Request(UUID uuid)
    {
        this.uuid = uuid;
        this.type = this.getClass().getSimpleName().toLowerCase(Locale.ROOT);
    }

    public abstract void visit(RequestVisitor visitor);

    @Override
    public String toString()
    {
        return "Request{" +
                "uuid=" + uuid +
                ", type='" + type + '\'' +
                '}';
    }
}
