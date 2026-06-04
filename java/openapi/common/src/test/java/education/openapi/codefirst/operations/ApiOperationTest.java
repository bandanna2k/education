package education.openapi.codefirst.operations;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiOperationTest
{
    @Test
    void toVertxPath_singleParam() {
        assertThat(ApiOperation.toVertxPath("/balance/{accountId}"))
                .isEqualTo("/balance/:accountId");
    }

    @Test
    void toVertxPath_multipleParams() {
        assertThat(ApiOperation.toVertxPath("/account/{accountId}/transaction/{transactionId}"))
                .isEqualTo("/account/:accountId/transaction/:transactionId");
    }

    @Test
    void toVertxPath_noParams() {
        assertThat(ApiOperation.toVertxPath("/health"))
                .isEqualTo("/health");
    }
}

