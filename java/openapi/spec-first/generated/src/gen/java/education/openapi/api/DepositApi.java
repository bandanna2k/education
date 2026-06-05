package education.openapi.api;

import education.openapi.model.Balance;
import education.openapi.model.ErrorResponse;
import education.openapi.model.TransactionRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

/**
* Represents a collection of functions to interact with the API endpoints.
*/
@Path("/deposit/{accountId}")
@Api(description = "the deposit API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.5.0")
public interface DepositApi extends Handler<RoutingContext> {

    @POST
    @Path("")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiResponse(responseCode = "200", description = "Updated balance")
    @ApiResponse(responseCode = "400", description = "Error")
    @RequestBody(required = true, description = "")
    /**
     * Deposit funds
     * Deposits the specified amount into the account
     *
     * @param ctx the routing context
     */
    public void handle(RoutingContext ctx);

}

