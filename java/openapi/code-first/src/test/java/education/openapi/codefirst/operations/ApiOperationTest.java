package education.openapi.codefirst.operations;

import org.junit.jupiter.api.Test;

import static education.openapi.operations.ToBeRenamed.toVertxPath;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiOperationTest
{
    @Test
    void toVertxPath_singleParam() {
        assertThat(toVertxPath("/balance/{accountId}"))
                .isEqualTo("/balance/:accountId");
    }

    @Test
    void toVertxPath_multipleParams() {
        assertThat(toVertxPath("/account/{accountId}/transaction/{transactionId}"))
                .isEqualTo("/account/:accountId/transaction/:transactionId");
    }

    @Test
    void toVertxPath_noParams() {
        assertThat(toVertxPath("/health"))
                .isEqualTo("/health");
    }
}

