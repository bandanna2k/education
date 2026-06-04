package education.openapi;

import education.openapi.codefirst.handlers.BalanceCommandHandler;
import education.openapi.codefirst.handlers.DepositCommand;
import education.openapi.codefirst.handlers.DepositCommandHandler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DepositDaoTest
{
    private DepositCommandHandler depositCommandHandler;
    private BalanceCommandHandler balanceCommandHandler;

    @BeforeEach
    void setUp() {

        final Map<Integer, BigDecimal> balances = new ConcurrentHashMap<>();
        depositCommandHandler = new DepositCommandHandler(balances);
        balanceCommandHandler = new BalanceCommandHandler(balances);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void shouldDeposit() {
        int accountId = 20;
        depositCommandHandler.handle(new DepositCommand(accountId, new BigDecimal("100")))
                .ifError(e -> Assertions.fail(e.toString()));
        balanceCommandHandler.handle(accountId)
                .ifError(e -> Assertions.fail(e.toString()));
    }
}
