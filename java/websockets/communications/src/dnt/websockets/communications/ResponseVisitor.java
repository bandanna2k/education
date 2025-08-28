package dnt.websockets.communications;

public interface ResponseVisitor
{
    default void visit(GetPropertyResponse response) {}
    default void visit(SetPropertyResponse response) {}
    default void visit(ErrorResponse response) {}
}
