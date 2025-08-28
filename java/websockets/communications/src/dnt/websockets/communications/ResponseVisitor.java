package dnt.websockets.communications;

interface ResponseVisitor
{
    default void visit(GetPropertyResponse response) {}
    default void visit(SetPropertyResponse response) {}
    default void visit(ErrorResponse response) {}

    default void visit(GetStatusResponse getStatusResponse) {}
}
