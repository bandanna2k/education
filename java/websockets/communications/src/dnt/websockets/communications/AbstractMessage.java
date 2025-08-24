package dnt.websockets.communications;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
public abstract class AbstractMessage
{
    public String type;

    public AbstractMessage()
    {
        this.type = this.getClass().getSimpleName();
    }

    public void visit(PushMessageVisitor visitor)
    {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "AbstractMessage{" +
                "type='" + type + '\'' +
                '}';
    }
}
