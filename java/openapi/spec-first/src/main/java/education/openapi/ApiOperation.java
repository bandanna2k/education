package education.openapi;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * Base interface for API operations.
 * All generated API interfaces extend this interface to provide common functionality.
 */
public interface ApiOperation extends Handler<RoutingContext>
{

}


