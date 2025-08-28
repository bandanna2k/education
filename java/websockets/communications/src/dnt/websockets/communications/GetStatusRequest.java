package dnt.websockets.communications;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GetStatusRequest extends AbstractRequest
{
    @JsonIgnore
    private String client;

    public GetStatusRequest()
    {
        super();
    }
    public GetStatusRequest(String client)
    {
        this();
        this.client = client;
    }

    @Override
    public void visit(ExecutionLayer executionLayer, MessageVisitor visitor)
    {
        visitor.visit(executionLayer, this);
    }

    @Override
    public String toString() {
        return "GetClientStatus{" +
                "client='" + client + '\'' +
                "} " + super.toString();
    }
}
