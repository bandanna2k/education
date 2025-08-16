package dnt.websockets.server.infrastructure;

public interface RequestVisitor
{
    default void visit(OptionsRequest optionsRequest) {}
}
