package dnt.websockets.server;

import dnt.websockets.infrastructure.ExecutionLayer;
import dnt.websockets.messages.*;

import java.util.HashMap;
import java.util.Optional;

/**
 * Class to handle requests
 */
public class ServerMessageProcessor implements MessageVisitor
{
    private final HashMap<String, String> properties = new HashMap<>();

    @Override
    public void visit(ExecutionLayer executionLayer, ClientPushPulse message)
    {
        System.out.println("Collect price: " + message);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, GetPropertyRequest request)
    {
        String value = properties.get(request.key);
        if(value == null)
        {
            final ErrorResponse response = new ErrorResponse(request.correlationId, 404, "Value not found");
            executionLayer.serverCompleteResponse(response);
        }
        else
        {
            GetPropertyResponse response = new GetPropertyResponse(request.correlationId, value);
            executionLayer.serverCompleteResponse(response);
        }
    }

    @Override
    public void visit(ExecutionLayer executionLayer, SetPropertyRequest request)
    {
        if("do_not_send_response".equals(request.key) && "true".equals(request.value))
        {
            return;
        }

        final Optional<ErrorResponse> maybeError = request.validate();
        if(maybeError.isPresent())
        {
            executionLayer.serverCompleteResponse(maybeError.get());
            return;
        }

        properties.put(request.key, request.value);
        SetPropertyResponse response = new SetPropertyResponse(request.correlationId);
        executionLayer.serverCompleteResponse(response);
    }

    @Override
    public void visit(ExecutionLayer executionLayer, GetStatusResponse message)
    {
        System.out.println("Get status response received.");
    }

    public String get(String key)
    {
        return properties.get(key);
    }
}
