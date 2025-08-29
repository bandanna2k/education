package dnt.websockets.integration.tcp.dsl;

import com.lmax.simpledsl.api.DslParams;
import com.lmax.simpledsl.api.OptionalArg;
import com.lmax.simpledsl.api.RequiredArg;
import dnt.websockets.messages.AbstractMessage;
import dnt.websockets.messages.GetPropertyResponse;
import dnt.websockets.messages.SetPropertyResponse;
import dnt.websockets.integration.tcp.TcpClientDriver;
import education.common.result.Result;
import io.vertx.core.Future;
import org.awaitility.Awaitility;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

public class TcpClientDsl
{
    private final TcpClientDriver clientDriver;

    public TcpClientDsl(TcpClientDriver clientDriver)
    {
        this.clientDriver = clientDriver;
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

    public void setProperty(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("key"),
                new RequiredArg("value"),
                new OptionalArg("expectSuccess").setDefault("true"));
        boolean expectSuccess = params.valueAsBoolean("expectSuccess");

        String key = params.value("key");
        String value = params.value("value");

        Result<SetPropertyResponse, String> result = join(clientDriver.setProperty(key, value));
        assertThat(result.isSuccess())
                .describedAs(result.toString())
                .isEqualTo(expectSuccess);
    }

    private <R> R join(Future<R> future)
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
                    AbstractMessage message = clientDriver.getLastMessage();
                    assertThat(message).isNotNull();
                    assertThat(message.getClass().getSimpleName()).isEqualToIgnoringCase(className);
                });
    }

    public void verifyNoMessage()
    {
        Awaitility
                .await()
                .pollInterval(ofMillis(100))
                .during(ofSeconds(2))
                .atMost(ofSeconds(3))
                .until(() -> {
                    AbstractMessage abstractMessage = clientDriver.getLastMessage();
                    return abstractMessage == null;
                });
    }
}
