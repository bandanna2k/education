package education.openapi.api.components;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AccountRequest",
        description = "Request object containing account information"
)
public class AccountRequest
{
    @Schema(
            description = "The unique account identifier",
            example = "123",
            type = "integer"
    )
    public int accountId;
}
