package education.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Deposit.class, name = "deposit"),
        @JsonSubTypes.Type(value = Withdrawal.class, name = "withdrawal")
})
public abstract class Message
{
    public String type;

    public abstract void visit(Visitor visitor);

    @Override
    public String toString()
    {
        return "Message{" +
                "type='" + type + '\'' +
                '}';
    }
}
