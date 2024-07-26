package casestudy.bank.projections;

import education.jackson.requests.Deposit;
import education.jackson.requests.Withdrawal;
import education.jackson.response.Balance;
import education.jackson.response.Balances;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;

public class AccountDao
{
    private final NamedParameterJdbcOperations jdbc;

    public AccountDao(DataSource dataSource)
    {
        jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    public void deposit(Deposit deposit)
    {
        try
        {
            System.out.println("DB adding " + deposit);
            jdbc.update(STR."""
                            insert into `common`.`balances` (account_id, balance) 
                            values (:accountId, :amount)
                            on duplicate key
                            update `balance` = (values(balance) + :amount)
                            """,
                    new MapSqlParameterSource()
                            .addValue("accountId", deposit.accountId)
                            .addValue("amount", deposit.amount));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void withdraw(Withdrawal withdrawal)
    {
        jdbc.update(STR."""
                insert into `common`.`balances` (account_id, balance) 
                values (:accountId, :amount)
                on duplicate key
                update `balance` = values(balance) + :amount
                """,
                new MapSqlParameterSource()
                        .addValue("accountId", withdrawal.accountId)
                        .addValue("amount", withdrawal.amount));
    }

    public Balances getBalances()
    {
        ArrayList<Balance> balances = new ArrayList<>();
        jdbc.query(STR."""
                select `account_id`, `balance`
                from `common`.`balances`
                """,
                new MapSqlParameterSource(),
                rs -> {
                    long accountId = rs.getLong("account_id");
                    BigDecimal balance = rs.getBigDecimal("balance");
                    balances.add(new Balance(null, accountId, balance));
                });
        return new Balances(null, balances);
    }
}
