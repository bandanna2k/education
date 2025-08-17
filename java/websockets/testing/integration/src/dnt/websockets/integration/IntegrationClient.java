package dnt.websockets.integration;

import dnt.websockets.client.ResponseProcessor;
import dnt.websockets.communications.OptionsRequest;
import dnt.websockets.server.RequestProcessor;

public class IntegrationClient
{
    IntegrationAdapter integrationAdapter = new IntegrationAdapter(
            RequestProcessor::new,
            new ResponseProcessor());

    public void fetchOptions()
    {
        integrationAdapter.send(OptionsRequest::new);
    }

    public void verifyOptions()
    {
    }
}
