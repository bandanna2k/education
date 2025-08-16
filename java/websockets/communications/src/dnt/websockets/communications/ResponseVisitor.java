package dnt.websockets.communications;

public interface ResponseVisitor
{
    default void visit(OptionsResponse optionsResponse) {}
}
