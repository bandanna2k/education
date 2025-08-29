package dnt.websockets.infrastructure;

import dnt.websockets.messages.AbstractMessage;

public interface Publisher
{
    void send(AbstractMessage message);
}
