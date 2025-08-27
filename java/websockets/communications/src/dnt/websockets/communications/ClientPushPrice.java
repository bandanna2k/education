package dnt.websockets.communications;

public class ClientPushPrice extends AbstractMessage
{
    public String symbol;
    public double price;
    public long sequence;

    public ClientPushPrice()
    {
        super();
    }
    public ClientPushPrice(String symbol, double price, long sequence)
    {
        this();
        this.symbol = symbol;
        this.price = price;
        this.sequence = sequence;
    }

    @Override
    public void visit(ExecutionLayer executionLayer, MessageVisitor visitor)
    {
        visitor.visit(executionLayer, this);
    }

    @Override
    public String toString()
    {
        return "ClientPushPrice{" +
                "symbol='" + symbol + '\'' +
                ", price=" + price +
                ", sequence=" + sequence +
                "} " + super.toString();
    }
}
