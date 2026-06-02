package education.openapi.codefirst.handlers;

import java.math.BigDecimal;

public record WithdrawalCommand(int accountId, BigDecimal amount) {
}
