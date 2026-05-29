package io.swagger.client.api;

import education.openapi.codefirst.components.AccountRequest;
import education.openapi.codefirst.components.Balance;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
* Represents a collection of functions to interact with the API endpoints.
*/
@Path("/balance")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.5.0")
public interface BalanceApi {

    /**
     * 
     *
     * @param accountRequest 
     * @return Current balance
     * @return Error
     */
    @GET
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    Balance getBalance(@Valid @NotNull AccountRequest accountRequest);

}
