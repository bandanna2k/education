package education.openapi.codefirst.operations;

import education.openapi.application.ToBeRenamed;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiOperationTest
{
    @Test
    void toVertxPath_singleParam() {
        assertThat(ToBeRenamed.toVertxPath("/balance/{accountId}"))
                .isEqualTo("/balance/:accountId");
    }

    @Test
    void toVertxPath_multipleParams() {
        assertThat(ToBeRenamed.toVertxPath("/account/{accountId}/transaction/{transactionId}"))
                .isEqualTo("/account/:accountId/transaction/:transactionId");
    }

    @Test
    void toVertxPath_noParams() {
        assertThat(ToBeRenamed.toVertxPath("/health"))
                .isEqualTo("/health");
    }
}

