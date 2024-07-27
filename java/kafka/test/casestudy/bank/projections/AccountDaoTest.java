package casestudy.bank.projections;

import education.jackson.requests.Deposit;
import education.jackson.requests.Withdrawal;
import education.jackson.response.Balances;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountDaoTest extends DaoTest
{

    private AccountDao accountDao;

    @Before
    public void setUp() throws Exception
    {
        clearTable("common.balances");

        accountDao = new AccountDao(getDataSource());
    }

    @Test
    public void deposit() { deposit(1); }
    public void deposit(long accountId)
    {
        accountDao.deposit(new Deposit(null, accountId, "50"));
        assertThat(accountDao.getBalance(accountId).success().balance.doubleValue()).isEqualTo(50.0);
    }

    @Test
    public void withdraw() { withdraw(2); }
    public void withdraw(long accountId)
    {
        accountDao.withdraw(new Withdrawal(null, accountId, "50"));
        assertThat(accountDao.getBalance(accountId).success().balance.doubleValue()).isEqualTo(-50.0);
    }

    @Test
    public void balances()
    {
        deposit(3);
        withdraw(4);

        Balances balances = accountDao.getBalances();
        balances.balances.stream().filter(balance -> balance.accountId == 3).findFirst().map(balance ->
                assertThat(balance.balance.doubleValue()).isEqualTo(50.0));
        balances.balances.stream().filter(balance -> balance.accountId == 4).findFirst().map(balance ->
                assertThat(balance.balance.doubleValue()).isEqualTo(-50.0));
    }
}