package education.openapi.codefirst.endpoints;

import education.openapi.codefirst.components.Balance;
import education.openapi.codefirst.components.TransactionRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/withdrawal")
public interface WithdrawalApi {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    Balance postWithdrawal(TransactionRequest transactionRequest);
}
