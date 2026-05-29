package education.openapi.codefirst.components;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "AccountRequest",
    description = "Request object containing account information"
)
public class AccountRequest
{
  @Schema(
      description = "The unique account identifier",
      example = "account-123"
  )
  public String accountId;
}
