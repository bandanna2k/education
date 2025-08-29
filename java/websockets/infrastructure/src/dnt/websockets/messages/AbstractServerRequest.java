package dnt.websockets.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Server request on the client
 */
public abstract class AbstractServerRequest extends AbstractRequest
{
    @JsonIgnore
    public String clientId;

    public AbstractServerRequest()
    {
        super();
    }
    public AbstractServerRequest(String clientId)
    {
        super();
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "AbstractServerRequest{" +
                "clientId='" + clientId + '\'' +
                "} " + super.toString();
    }
}
