package education.openapi.specfirst.generated.api;

import education.openapi.specfirst.generated.model.Balance;
import education.openapi.specfirst.generated.model.ErrorResponse;
import education.openapi.specfirst.generated.model.TransactionRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import education.openapi.ApiOperation;
import io.vertx.ext.web.RoutingContext;


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
public interface WithdrawalApi extends ApiOperation {

    /**
     * Withdraw funds
     * 
     *
     * @param ctx the routing context
     */
    public void handle(io.vertx.ext.web.RoutingContext ctx);


}

