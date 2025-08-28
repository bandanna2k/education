package dnt.websockets.communications;

public class GetStatusResponse extends AbstractResponse
{
    public String status;

    public GetStatusResponse()
    {
        super();
    }
    public GetStatusResponse(String status)
    {
        this();
        this.status = status;
    }

    @Override
    public void visit(ExecutionLayer executionLayer, MessageVisitor visitor)
    {
        visitor.visit(executionLayer, this);
    }

    @Override
    public String toString() {
        return "GetStatusResponse{" +
                "status='" + status + '\'' +
                "} " + super.toString();
    }
}
