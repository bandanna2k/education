package dnt.websockets.messages;

import dnt.websockets.infrastructure.ExecutionLayer;

public interface MessageVisitor
{
    MessageVisitor NO_OP = new MessageVisitor() {};

    // Both
    default void visit(ExecutionLayer executionLayer, AbstractMessage message) {}

    // Server to Client
    default void visit(ExecutionLayer executionLayer, ServerPushMessage message) {}
    default void visit(ExecutionLayer executionLayer, GetStatusRequest request) {}

    default void visit(ExecutionLayer executionLayer, ErrorResponse response) {}
    default void visit(ExecutionLayer executionLayer, GetPropertyResponse response) {}
    default void visit(ExecutionLayer executionLayer, SetPropertyResponse response) {}

    // Client to Server
    default void visit(ExecutionLayer executionLayer, GetPropertyRequest request) {}
    default void visit(ExecutionLayer executionLayer, SetPropertyRequest request) {}
    default void visit(ExecutionLayer executionLayer, ClientPushPulse message) {}
    default void visit(ExecutionLayer executionLayer, GetStatusResponse message) {}
}
