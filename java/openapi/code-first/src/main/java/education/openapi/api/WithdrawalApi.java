package education.openapi.api;

import education.openapi.api.components.Balance;
import education.openapi.api.components.TransactionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/withdrawal/{accountId}")
public interface WithdrawalApi extends Handler<RoutingContext>
{
    @POST
    @Operation(
        operationId = "postWithdrawal",
        summary = "Withdraw funds",
        description = "Withdraws the specified amount from the account",
        tags = {"Withdrawal"},
        parameters = {
            @Parameter(name = "accountId", in = ParameterIn.PATH, required = true, description = "The unique account identifier", schema = @Schema(type = "integer"))
        }
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
        description = "Withdrawal completed successfully",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Balance.class)
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Withdrawal failed (insufficient funds or invalid request)",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(name = "ErrorResponse")
        )
    )
    void handle(RoutingContext event);
}
