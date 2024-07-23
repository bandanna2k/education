package education.jackson;

public interface Visitor
{
    default void visit(Deposit deposit) {}

    default void visit(Withdrawal withdrawal) {}
}
