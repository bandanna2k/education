package education.vertx.json.registersubtypes.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type", visible = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Response
{
    public UUID uuid;

    public Response()
    {
    }
    public Response(UUID uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public String toString()
    {
        return "Request{" +
                "uuid=" + uuid +
                '}';
    }
}
