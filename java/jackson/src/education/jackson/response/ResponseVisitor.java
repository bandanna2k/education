package education.jackson.response;

import education.jackson.requests.Deposit;
import education.jackson.requests.Withdrawal;

public interface ResponseVisitor
{
    default void visit(Balance balance) {}

    default void visit(Error error) {}
}
