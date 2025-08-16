package dnt.websockets.client;

import dnt.websockets.communications.*;

public class ResponseProcessor implements ResponseVisitor
{
    @Override
    public void visit(OptionsResponse optionsResponse)
    {
        System.out.println("PROCESSING RESPONSE");
    }
}
