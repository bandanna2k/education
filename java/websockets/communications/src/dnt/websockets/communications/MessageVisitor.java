package dnt.websockets.communications;

public interface MessageVisitor
{
    MessageVisitor NO_OP = new MessageVisitor() {};

    // Both
    default void visit(ExecutionLayer executionLayer, AbstractMessage message) {}

    // Server to Client
    default void visit(ExecutionLayer executionLayer, ServerPushMessage message) {}
    default void visit(ExecutionLayer executionLayer, GetStatusRequest request) {}
    default void visit(ExecutionLayer executionLayer, ErrorResponse response) {}

    // Client to Server
    default void visit(ExecutionLayer executionLayer, GetPropertyRequest request) {}
    default void visit(ExecutionLayer executionLayer, SetPropertyRequest request) {}
    default void visit(ExecutionLayer executionLayer, ClientPushPrice message) {}
}
