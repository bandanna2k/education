package education.vertx.json.subtypes.unannotated.response;

import java.util.UUID;

public class Error extends Response
{
    public String error;

    public Error()
    {
        this(null, null);
    }
    public Error(UUID uuid, String error)
    {
        super(uuid);
        this.error = error;
    }

    @Override
    public String toString()
    {
        return "Error{" +
                "error='" + error + '\'' +
                "} " + super.toString();
    }
}
