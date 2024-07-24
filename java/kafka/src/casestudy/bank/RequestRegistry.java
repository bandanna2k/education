package casestudy.bank;

import education.jackson.requests.Deposit;
import education.jackson.requests.RequestVisitor;
import education.jackson.requests.Withdrawal;

import java.util.ArrayList;
import java.util.List;

public class RequestRegistry implements RequestVisitor
{
    private final List<DepositListener> depositListeners = new ArrayList<>();
    private final List<WithdrawalListener> withdrawalListeners = new ArrayList<>();

    @Override
    public void visit(Deposit deposit)
    {
        depositListeners.forEach(listener -> listener.onDeposit(deposit));
    }
    public void subscribe(DepositListener depositListener)
    {
        depositListeners.add(depositListener);
    }

    @Override
    public void visit(Withdrawal withdrawal)
    {
        withdrawalListeners.forEach(listener -> listener.onWithdrawal(withdrawal));
    }
    public void subscribe(WithdrawalListener withdrawalListener)
    {
        withdrawalListeners.add(withdrawalListener);
    }

    public interface DepositListener
    {
        void onDeposit(Deposit deposit);
    }

    public interface WithdrawalListener
    {
        void onWithdrawal(Withdrawal withdrawal);
    }
}
