package dnt.websockets.messages;

import dnt.websockets.infrastructure.ExecutionLayer;

public class ClientPushPulse extends AbstractMessage
{
    public long rate;
    public long sequence;

    public ClientPushPulse()
    {
        super();
    }
    public ClientPushPulse(int rate, long sequence)
    {
        this();
        this.rate = rate;
        this.sequence = sequence;
    }

    @Override
    public void visit(ExecutionLayer executionLayer, MessageVisitor visitor)
    {
        visitor.visit(executionLayer, this);
    }

    @Override
    public String toString() {
        return "ClientPushPulse{" +
                "rate=" + rate +
                ", sequence=" + sequence +
                "} " + super.toString();
    }
}
