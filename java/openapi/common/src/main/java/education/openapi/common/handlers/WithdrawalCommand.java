package education.openapi.common.handlers;

import java.math.BigDecimal;

public record WithdrawalCommand(int accountId, BigDecimal amount) {
}
