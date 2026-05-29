package education.openapi.codefirst.endpoints;

import education.openapi.codefirst.components.AccountRequest;
import education.openapi.codefirst.components.Balance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/balance")
@Tag(
    name = "Balance",
    description = "Get account balance information"
)
public interface BalanceApi {

    @GET
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @Operation(
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
    Balance getBalance(AccountRequest accountRequest);
}
