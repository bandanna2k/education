package dnt.websockets.integration.dsl;

import com.lmax.simpledsl.api.DslParams;
import com.lmax.simpledsl.api.OptionalArg;
import com.lmax.simpledsl.api.RequiredArg;
import dnt.websockets.integration.IntegrationExecutionLayer;

public class IntegrationDsl
{
    private final IntegrationExecutionLayer executionLayer;

    public IntegrationDsl(IntegrationExecutionLayer executionLayer)
    {
        this.executionLayer = executionLayer;
    }

    public void failNextMessage(String failureMessage)
    {
        this.executionLayer.failNextMessage(failureMessage);
    }

    public void throwOnNextMessage()
    {
        this.executionLayer.throwOnNextMessage();
    }

    public void pauseProcessing()
    {
        this.executionLayer.pauseProcessing();
    }

    public void resumeProcessing(String... args)
    {
        final DslParams params = DslParams.create(args,
                new OptionalArg("messageCount").setDefault("99999999"));
        this.executionLayer.resumeProcessing(params.valueAsInt("messageCount"));
    }

    public boolean isComplete()
    {
        return this.executionLayer.isComplete();
    }
}
