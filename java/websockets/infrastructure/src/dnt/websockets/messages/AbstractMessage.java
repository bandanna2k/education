package dnt.websockets.messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dnt.websockets.infrastructure.ExecutionLayer;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
public abstract class AbstractMessage
{
    public String type;

    public AbstractMessage()
    {
        this.type = this.getClass().getSimpleName();
    }

    public abstract void visit(ExecutionLayer executionLayer, MessageVisitor visitor);

    @Override
    public String toString() {
        return "AbstractMessage{" +
                "type='" + type + '\'' +
                '}';
    }
}
