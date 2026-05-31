package education.openapi.codefirst.operations;

import education.openapi.codefirst.components.Balance;
import education.openapi.codefirst.components.TransactionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.vertx.ext.web.RoutingContext;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/deposit")
public interface DepositOperation extends ApiOperation {

    @POST
    @Operation(
        operationId = "postDeposit",
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
    @Override
    void handle(RoutingContext ctx);
}
