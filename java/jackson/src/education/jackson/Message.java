package education.jackson;

import com.fasterxml.jackson.annotation.*;

import java.util.Locale;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Deposit.class, name = "deposit"),
        @JsonSubTypes.Type(value = Withdrawal.class, name = "withdrawal")
})
public abstract class Message
{
    public String type;

    public Message()
    {
        type = this.getClass().getSimpleName().toLowerCase(Locale.ROOT);
    }

    public abstract void visit(MessageVisitor visitor);

    @Override
    public String toString()
    {
        return "Message{" +
                "type='" + type + '\'' +
                '}';
    }
}
