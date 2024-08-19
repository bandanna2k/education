package education.vertx.json.subtypes.unannotated.requests;

import java.util.Locale;
import java.util.UUID;

public abstract class Request
{
    public UUID uuid;
    public String type;

    public Request()
    {
    }
    public Request(UUID uuid)
    {
        this.uuid = uuid;
        this.type = this.getClass().getSimpleName().toLowerCase(Locale.ROOT);
    }

    @Override
    public String toString()
    {
        return "Request{" +
                "uuid=" + uuid +
                ", type='" + type + '\'' +
                '}';
    }
}
