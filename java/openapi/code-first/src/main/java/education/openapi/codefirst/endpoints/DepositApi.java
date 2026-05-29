package education.openapi.codefirst.endpoints;

import education.openapi.codefirst.components.Balance;
import education.openapi.codefirst.components.TransactionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/deposit")
@Tag(
    name = "Deposit",
    description = "Deposit funds into an account"
)
public interface DepositApi {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @Operation(
        summary = "Deposit funds",
        description = "Deposits the specified amount into the account",
        tags = {"Deposit"}
    )
    @RequestBody(
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TransactionRequest.class)
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Deposit completed successfully",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Balance.class)
        )
    )
    Balance postDeposit(@Valid @NotNull TransactionRequest transactionRequest);
}
