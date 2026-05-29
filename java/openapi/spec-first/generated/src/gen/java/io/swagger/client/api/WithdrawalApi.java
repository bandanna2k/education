package io.swagger.client.api;

import io.swagger.client.model.Balance;
import io.swagger.client.model.ErrorResponse;
import io.swagger.client.model.TransactionRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

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
