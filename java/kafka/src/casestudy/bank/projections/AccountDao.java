package casestudy.bank.projections;

import education.common.result.Result;
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
import java.util.List;

import static education.common.result.Result.success;

public class AccountDao
{
    private final NamedParameterJdbcOperations jdbc;

    public AccountDao(DataSource dataSource)
    {
        jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    public Result<Void, Void> deposit(Deposit deposit)
    {
        jdbc.update(STR."""
                        insert into `common`.`balances` (account_id, balance) 
                        values (:accountId, :amount)
                        on duplicate key
                        update `balance` = (balance + :amount)
                        """,
                new MapSqlParameterSource()
                        .addValue("accountId", deposit.accountId)
                        .addValue("amount", deposit.amount));
        return success(null);
    }

    public Result<Void, Void> withdraw(Withdrawal withdrawal)
    {
        jdbc.update(STR."""
                insert into `common`.`balances` (account_id, balance) 
                values (:accountId, :amount)
                on duplicate key
                update `balance` = (balance + :amount)
                """,
                new MapSqlParameterSource()
                        .addValue("accountId", withdrawal.accountId)
                        .addValue("amount", BigDecimal.ZERO.subtract(withdrawal.amount)));
        return success(null);
    }

    public Balances getBalances()
    {
        List<Balance> balances = new ArrayList<>();
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

    public Result<Balance, String> getBalance(long accountId)
    {
        List<Balance> balances = new ArrayList<>();
        jdbc.query(STR."""
                select `account_id`, `balance`
                from `common`.`balances`
                where account_id = :accountId
                """,
                new MapSqlParameterSource("accountId", accountId),
                rs -> {
                    balances.add(new Balance(null,
                            rs.getLong("account_id"),
                            rs.getBigDecimal("balance")));
                });
        if(balances.size() == 0) return Result.failure("Account not found.");
        if(balances.size() == 1) return success(balances.get(0));
        return Result.failure("Too many results");
    }
}
