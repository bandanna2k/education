package education.openapi.application.handlers;

import java.math.BigDecimal;

public record WithdrawalCommand(int accountId, BigDecimal amount) {
}
