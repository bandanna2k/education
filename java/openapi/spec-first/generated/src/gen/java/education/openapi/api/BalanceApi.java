package education.openapi.api;

import education.openapi.model.Balance;
import education.openapi.model.ErrorResponse;

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
@Path("/balance/{accountId}")
@Api(description = "the balance API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.5.0")
public interface BalanceApi extends Handler<RoutingContext> {

    @GET
    @Path("")
    @Produces({ "application/json" })
    @ApiResponse(responseCode = "200", description = "Current balance")
    @ApiResponse(responseCode = "400", description = "Error")
    /**
     * Get account balance
     * Retrieves the current balance for a given account
     *
     * @param ctx the routing context
     */
    public void handle(RoutingContext ctx);

}

