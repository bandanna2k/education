package education.openapi.codefirst.endpoints;

import education.openapi.codefirst.components.Balance;
import education.openapi.codefirst.components.TransactionRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/deposit")
public interface DepositApi {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    Balance postDeposit(@Valid @NotNull TransactionRequest transactionRequest);
}
