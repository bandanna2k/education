package casestudy.bank.projections;

import education.jackson.requests.Deposit;
import education.jackson.requests.Withdrawal;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

public class AccountDao
{
    private final NamedParameterJdbcOperations jdbc;

    public AccountDao(DataSource dataSource)
    {
        jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    public void deposit(Deposit deposit)
    {
        jdbc.update(STR."""
                insert into `common`.`balances` (account_id, balance) 
                values (:accountId, :amount)
                on duplicate key
                update `balance` = values(balance) + :amount
                """,
                new MapSqlParameterSource()
                        .addValue("accountId", deposit.accountId)
                        .addValue("amount", deposit.amount));
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
}
