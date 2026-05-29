package education.openapi.codefirst.endpoints;

import education.openapi.codefirst.components.AccountRequest;
import education.openapi.codefirst.components.Balance;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/balance")
public interface BalanceApi {

    @GET
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    Balance getBalance(AccountRequest accountRequest);
}
