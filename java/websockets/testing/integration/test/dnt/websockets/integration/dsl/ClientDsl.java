package dnt.websockets.integration.dsl;

import com.lmax.simpledsl.api.DslParams;
import com.lmax.simpledsl.api.OptionalArg;
import com.lmax.simpledsl.api.RequiredArg;
import dnt.websockets.integration.ClientDriver;
import dnt.websockets.integration.MessageCollector;
import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.GetPropertyResponse;
import dnt.websockets.messages.SetPropertyResponse;
import education.common.result.Result;
import io.vertx.core.Future;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class ClientDsl
{
    private final ClientDriver clientDriver;
    private final MessageCollector collector;

    public ClientDsl(ClientDriver clientDriver, MessageCollector collector)
    {
        this.clientDriver = clientDriver;
        this.collector = collector;
    }

    public void getProperty(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("key"),
                new OptionalArg("expectedValue"),
                new OptionalArg("expectSuccess").setDefault("true"),
                new OptionalArg("expectException").setDefault("false"));
        boolean expectSuccess = params.valueAsBoolean("expectSuccess");

        String key = params.value("key");
        if(params.valueAsBoolean("expectException"))
        {
            Assertions.assertThatException()
                    .isThrownBy(() -> join(clientDriver.getProperty(key)))
                    .withCauseInstanceOf(RuntimeException.class);
            return;
        }

        Result<GetPropertyResponse, String> result = join(clientDriver.getProperty(key));
        assertThat(result.isSuccess())
                .describedAs(result.toString())
                .isEqualTo(expectSuccess);
        params.valueAsOptional("expectedValue").ifPresent(expectedValue ->
                assertThat(result.success().value).isEqualTo(expectedValue));
    }

    public void setProperty(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("key"),
                new RequiredArg("value"),
                new OptionalArg("complete").setDefault("true"),
                new OptionalArg("expectSuccess").setDefault("true"),
                new OptionalArg("expectedErrorMessage"));
        boolean expectSuccess = params.valueAsBoolean("expectSuccess");

        String key = "<NULL>".equals(params.value("key")) ? null : params.value("key");
        String value = "<NULL>".equals(params.value("value")) ? null : params.value("value");
        boolean complete = params.valueAsBoolean("complete");

        Future<Result<SetPropertyResponse, String>> future = clientDriver.setProperty(key, value);
        if(complete)
        {
            Result<SetPropertyResponse, String> result = join(future);
            assertThat(result.isSuccess()).isEqualTo(expectSuccess);
            result.ifError(actualError ->
                    params.valueAsOptional("expectedErrorMessage").ifPresent(expectedErrorMessage -> {
                        assertThat(actualError).isEqualTo(expectedErrorMessage);
                    }));
        }
    }

    private static <R> R join(Future<R> future)
    {
        return future.toCompletionStage().toCompletableFuture().join();
    }

    public void verifyMessage(String className)
    {
        AbstractMessage lastMessage = collector.getLastMessage();
        assertThat(lastMessage).isNotNull();
        assertThat(lastMessage.getClass().getSimpleName()).isEqualTo(className);
    }

    public void verifyNoMoreMessages()
    {
        AbstractMessage lastMessage = collector.getLastMessage();
        assertThat(lastMessage).isNull();
    }

    public void pushPulse(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("rate"),
                new RequiredArg("sequence"));

        int rate = params.valueAsInt("rate");
        long sequence = params.valueAsLong("sequence");
        clientDriver.pushPulse(rate, sequence);
    }

    public void setStatus(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("rate"));
        clientDriver.setStatus(params.value("rate"));
    }
}
