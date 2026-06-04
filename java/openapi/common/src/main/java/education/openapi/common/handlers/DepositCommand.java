package education.openapi.common.handlers;

import java.math.BigDecimal;

public record DepositCommand(int accountId, BigDecimal amount) {
}
