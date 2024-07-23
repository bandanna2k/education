package education.jackson;

public interface MessageVisitor
{
    default void visit(Deposit deposit) {}

    default void visit(Withdrawal withdrawal) {}
}
