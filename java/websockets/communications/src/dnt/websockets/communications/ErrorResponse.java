package dnt.websockets.communications;

public class ErrorResponse extends AbstractResponse
{
    public int statusCode;
    public String message;


    public ErrorResponse() {}
    public ErrorResponse(long correlationId, int statusCode, String message)
    {
        super(correlationId);
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public void visit(ResponseVisitor visitor)
    {
        visitor.visit(this);
    }

    @Override
    public String toString()
    {
        return "ErrorResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                "} " + super.toString();
    }
}
