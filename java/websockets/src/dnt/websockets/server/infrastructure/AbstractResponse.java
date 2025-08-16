package dnt.websockets.server.infrastructure;

public abstract class AbstractResponse extends AbstractMessage
{
    public long correlationId;
    public String type;

    public AbstractResponse(long correlationId)
    {
        this.correlationId = correlationId;
        this.type = this.getClass().getSimpleName();
    }

    @Override
    public String toString()
    {
        return "AbstractResponse{" +
                "correlationId=" + correlationId +
                ", type='" + type + '\'' +
                '}';
    }
}
