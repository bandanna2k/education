package education.openapi.codefirst.operations;

import education.openapi.codefirst.operations.components.AccountRequest;
import education.openapi.codefirst.operations.components.Balance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/balance")
public interface BalanceOperation extends ApiOperation<Balance, AccountRequest>{

    @GET
    @Operation(
        operationId = "getBalance",
        summary = "Get account balance",
        description = "Retrieves the current balance for a given account",
        tags = {"Balance"}
    )
    @RequestBody(
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = AccountRequest.class)
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Balance retrieved successfully",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Balance.class)
        )
    )
    Balance execute(AccountRequest accountRequest);
}
