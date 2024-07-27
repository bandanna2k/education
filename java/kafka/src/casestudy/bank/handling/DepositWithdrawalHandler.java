package casestudy.bank.handling;

import casestudy.bank.projections.AccountDao;
import casestudy.bank.publishers.ResponsePublisher;
import casestudy.bank.RequestRegistry;
import education.common.result.Result;
import education.jackson.requests.Deposit;
import education.jackson.requests.Withdrawal;
import education.jackson.response.Balance;
import education.jackson.response.Error;

import java.math.BigDecimal;

import static education.common.result.Result.failure;
import static education.common.result.Result.success;

public class DepositWithdrawalHandler implements RequestRegistry.DepositListener, RequestRegistry.WithdrawalListener
{
    private final ResponsePublisher publisher;
    private final AccountDao dao;

    public DepositWithdrawalHandler(ResponsePublisher publisher, AccountDao dao)
    {
        this.publisher = publisher;
        this.dao = dao;
    }

    @Override
    public void onDeposit(Deposit deposit)
    {
//        System.out.println("Deposit " + deposit);

        dao.getBalance(deposit.accountId).fold(
                currentBalance ->
                    dao.deposit(deposit).fold(
                            success ->
                            {
                                BigDecimal balanceAfterDeposit = currentBalance.balance.add(deposit.amount);
                                publisher.publishResponse(new Balance(deposit.uuid, deposit.accountId, balanceAfterDeposit));
                            },
                            unused -> { throw new RuntimeException("Code unreachable"); }),
                error -> publisher.publishResponse(new Error(deposit.uuid, error))
        );

//        System.out.println("Deposit result:" + result);
    }

    @Override
    public void onWithdrawal(Withdrawal withdrawal)
    {
//        System.out.println("Withdrawal " + withdrawal);
//
        dao.getBalance(withdrawal.accountId).fold(
                currentBalance ->
                        validateWithdrawal(currentBalance, withdrawal).fold(
                                balanceAfterWithdrawal ->
                                        dao.withdraw(withdrawal).fold(
                                                success -> publisher.publishResponse(new Balance(withdrawal.uuid, withdrawal.accountId, balanceAfterWithdrawal)),
                                                unused -> { throw new RuntimeException("Code unreachable"); }),
                                error -> publisher.publishResponse(new Error(withdrawal.uuid, error))),
                error -> publisher.publishResponse(new Error(withdrawal.uuid, error))
        );
    }

    private Result<BigDecimal, String> validateWithdrawal(Balance currentBalance, Withdrawal withdrawal)
    {
        BigDecimal balanceAfterWithdrawal = currentBalance.balance.subtract(withdrawal.amount);
        if(balanceAfterWithdrawal.compareTo(BigDecimal.ZERO) < 0)
        {
            return failure("Not enough equity to withdraw.");
        }
        return success(balanceAfterWithdrawal);
    }
}
