package education.openapi.codefirst.endpoints;

import education.openapi.codefirst.components.Balance;
import education.openapi.codefirst.components.TransactionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/withdrawal")
public interface WithdrawalApi {

    @POST
    @Operation(
        summary = "Withdraw funds",
        description = "Withdraws the specified amount from the account",
        tags = {"Withdrawal"}
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
    Balance postWithdrawal(TransactionRequest transactionRequest);
}
