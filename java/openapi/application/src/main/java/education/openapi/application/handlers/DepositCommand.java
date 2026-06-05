package education.openapi.application.handlers;

import java.math.BigDecimal;

public record DepositCommand(int accountId, BigDecimal amount) {
}
