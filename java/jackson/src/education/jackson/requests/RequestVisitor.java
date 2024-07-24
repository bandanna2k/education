package education.jackson.requests;

public interface RequestVisitor
{
    default void visit(Deposit deposit) {}

    default void visit(Withdrawal withdrawal) {}
}
