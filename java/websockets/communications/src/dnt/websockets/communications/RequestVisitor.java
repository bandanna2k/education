package dnt.websockets.communications;

public interface RequestVisitor
{
    default void visit(OptionsRequest optionsRequest) {}
}
