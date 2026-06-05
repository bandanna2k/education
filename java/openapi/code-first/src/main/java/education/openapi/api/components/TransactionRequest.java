package education.openapi.api.components;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "TransactionRequest",
        description = "Request object for deposit and withdrawal transactions"
)
public class TransactionRequest
{
    @Schema(
            description = "The unique account identifier",
            example = "123",
            type = "integer"
    )
    public int accountId;

    @Schema(
            description = "The transaction amount as a string",
            example = "50.00"
    )
    public String amount;
}
