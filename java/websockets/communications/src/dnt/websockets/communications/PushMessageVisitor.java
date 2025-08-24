package dnt.websockets.communications;

public interface PushMessageVisitor
{
    void visit(PushMessage pushMessage);

    void visit(AbstractMessage abstractMessage);
}
