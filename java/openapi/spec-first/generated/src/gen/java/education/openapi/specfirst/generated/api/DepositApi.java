package education.openapi.specfirst.generated.api;

import education.openapi.specfirst.generated.model.Balance;
import education.openapi.specfirst.generated.model.ErrorResponse;
import education.openapi.specfirst.generated.model.TransactionRequest;

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
@Path("/deposit")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.5.0")
public interface DepositApi {

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
    Balance postDeposit(@Valid @NotNull TransactionRequest transactionRequest);

}
