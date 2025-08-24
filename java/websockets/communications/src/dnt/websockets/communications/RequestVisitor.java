package dnt.websockets.communications;

public interface RequestVisitor
{
    default void visit(ExecutionLayer executionLayer, GetPropertyRequest request) {}
    default void visit(ExecutionLayer executionLayer, SetPropertyRequest request) {}
}
