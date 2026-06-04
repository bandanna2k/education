package education.openapi.operations.components;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "Balance",
    description = "Account balance information"
)
public class Balance
{
    @Schema(
        description = "The balance amount as a string",
        example = "100.50"
    )
    public String balance;

  public Balance()
  {
  }

  public Balance(String balance)
  {
    this.balance = balance;
  }
}

