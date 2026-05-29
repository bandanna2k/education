package io.swagger.client.api;

import education.openapi.codefirst.components.Balance;
import education.openapi.codefirst.components.TransactionRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
* Represents a collection of functions to interact with the API endpoints.
*/
@Path("/withdrawal")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.5.0")
public interface WithdrawalApi {

    /**
     * 
     *
     * @param transactionRequest 
     * @return Updated balance
     * @return Error
     */
    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    Balance postWithdrawal(@Valid @NotNull TransactionRequest transactionRequest);

}
