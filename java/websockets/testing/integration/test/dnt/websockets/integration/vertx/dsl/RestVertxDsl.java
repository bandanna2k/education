package dnt.websockets.integration.vertx.dsl;

import com.lmax.simpledsl.api.DslParams;
import com.lmax.simpledsl.api.OptionalArg;
import com.lmax.simpledsl.api.RequiredArg;
import dnt.websockets.integration.vertx.VertxRestDriver;
import io.vertx.core.Future;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class RestVertxDsl
{
    private final VertxRestDriver restDriver;

    public RestVertxDsl(VertxRestDriver restDriver)
    {
        this.restDriver = restDriver;
    }

    public void getProperty(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("key"),
                new OptionalArg("expectedValue"),
                new OptionalArg("expectedStatusCode").setDefault("200"));
        String key = params.value("key");
        String actualValue = join(restDriver.getProperty(key, params.valueAsInt("expectedStatusCode")));

        Optional<String> maybeExpectedValue = params.valueAsOptional("expectedValue");
        maybeExpectedValue.ifPresent(expectedValue ->
                assertThat(maybeExpectedValue.orElse(null)).isEqualTo(actualValue));
    }

    public void setProperty(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("key"),
                new RequiredArg("value"),
                new OptionalArg("expectedStatusCode").setDefault("200"));
        String key = params.value("key");
        String value = params.value("value");
        restDriver.setProperty(key, value, params.valueAsInt("expectedStatusCode"));
    }

    private <R> R join(Future<R> future)
    {
        return future.toCompletionStage().toCompletableFuture().join();
    }
}
