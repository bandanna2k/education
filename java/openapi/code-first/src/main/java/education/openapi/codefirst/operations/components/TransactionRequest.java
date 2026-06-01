package education.openapi.codefirst.operations.components;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "TransactionRequest",
    description = "Request object for deposit and withdrawal transactions"
)
public class TransactionRequest
{
  @Schema(
      description = "The unique account identifier",
      example = "account-123"
  )
  public String accountId;

  @Schema(
      description = "The transaction amount as a string",
      example = "50.00"
  )
  public String amount;
}
