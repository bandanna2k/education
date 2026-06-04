package education.openapi.operations;

import education.openapi.operations.components.AccountRequest;
import education.openapi.operations.components.Balance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.vertx.ext.web.RoutingContext;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/balance/{accountId}")
public interface BalanceOperation extends ApiOperation
{
    @GET
    @Operation(
        operationId = "getBalance",
        summary = "Get account balance",
        description = "Retrieves the current balance for a given account",
        tags = {"Balance"},
        parameters = {
            @Parameter(name = "accountId", in = ParameterIn.PATH, required = true, description = "The unique account identifier", schema = @Schema(type = "integer"))
        }
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
    void handle(RoutingContext ctx);
}
