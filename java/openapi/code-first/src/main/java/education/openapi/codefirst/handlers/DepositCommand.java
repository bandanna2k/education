package education.openapi.codefirst.handlers;

import java.math.BigDecimal;

public record DepositCommand(int accountId, BigDecimal amount) {
}
