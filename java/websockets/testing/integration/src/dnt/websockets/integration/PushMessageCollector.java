package dnt.websockets.integration;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.PushMessage;
import dnt.websockets.communications.PushMessageVisitor;

import java.util.LinkedList;
import java.util.Queue;

public class PushMessageCollector implements PushMessageVisitor
{
    private final Queue<AbstractMessage> messages = new LinkedList<>();

    @Override
    public void visit(PushMessage pushMessage)
    {
        messages.add(pushMessage);
    }

    @Override
    public void visit(AbstractMessage message)
    {
        messages.add(message);
    }

    public <T extends AbstractMessage> T getLastMessage()
    {
        if(messages.isEmpty())
        {
            return null;
        }
        return (T) messages.remove();
    }
}
