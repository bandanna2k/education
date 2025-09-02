package dnt.websockets.integration.vertx.dsl;

import com.lmax.simpledsl.api.DslParams;
import com.lmax.simpledsl.api.OptionalArg;
import com.lmax.simpledsl.api.RequiredArg;
import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.GetPropertyResponse;
import dnt.websockets.messages.SetPropertyResponse;
import dnt.websockets.integration.vertx.WebSocketClientDriver;
import education.common.result.Result;
import io.vertx.core.Future;
import org.awaitility.Awaitility;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClientWebSocketDsl
{
    private final WebSocketClientDriver clientDriver;

    public ClientWebSocketDsl(WebSocketClientDriver clientDriver)
    {
        this.clientDriver = clientDriver;
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
            result.consume(response -> assertTrue(expectSuccess),
                    error ->
                    {
                        assertFalse(expectSuccess);
                        params.valueAsOptional("expectedErrorMessage").ifPresent(expectedErrorMessage ->
                                assertThat(error).isEqualTo(expectedErrorMessage));
                    });
        }
    }

    public void getProperty(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("key"),
                new OptionalArg("expectedValue"),
                new OptionalArg("expectSuccess").setDefault("true"));
        boolean expectSuccess = params.valueAsBoolean("expectSuccess");

        String key = params.value("key");
        Result<GetPropertyResponse, String> result = join(clientDriver.getProperty(key));
        assertThat(result.isSuccess()).isEqualTo(expectSuccess);
        params.valueAsOptional("expectedValue").ifPresent(expectedValue ->
                assertThat(result.success().value).isEqualTo(expectedValue));
    }

    private static <R> R join(Future<R> future)
    {
        return future.toCompletionStage().toCompletableFuture().join();
    }

    public void verifyMessage(String className)
    {
        Awaitility
                .await()
                .pollInterval(ofMillis(100))
                .atMost(ofSeconds(2))
                .untilAsserted(() ->
                {
                    AbstractMessage message = clientDriver.popLastMessage();
                    assertThat(message).isNotNull();
                    assertThat(message.getClass().getSimpleName()).isEqualToIgnoringCase(className);
                });
    }

    public void verifyNoMoreMessages()
    {
        Awaitility
                .await()
                .pollInterval(ofMillis(100))
                .during(ofMillis(2000))
                .until(() -> {
                    AbstractMessage abstractMessage = clientDriver.popLastMessage();
                    return abstractMessage == null;
                });
    }

    public void setStatus(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("status"));
        clientDriver.setStatus(params.value("status"));
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
}
