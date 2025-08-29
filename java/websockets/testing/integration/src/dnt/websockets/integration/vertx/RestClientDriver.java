package dnt.websockets.integration.vertx;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import java.util.Map;

public class RestClientDriver
{
    private static final int PORT = 7780;
    private static final String HOST = "localhost";

    private final WebClient client;

    public RestClientDriver(Vertx vertx)
    {
        client = WebClient.create(vertx);
    }

    public Future<String> getProperty(String key, int expectedStatusCode)
    {
        return client
                .get(PORT, HOST, "/property")
                .addQueryParam("key", key)
                .timeout(2000)
                .send()
                .map(response -> {
                    int actualStatusCode = response.statusCode();
                    if (actualStatusCode != expectedStatusCode)
                        throw new RuntimeException(String.format("Unexpected status code. Expected: %d, Actual: %d %s",
                                expectedStatusCode, actualStatusCode, response.body()));

                    if(response.statusCode() < 300)
                    {
                        JsonObject json = response.bodyAsJsonObject();
                        return String.valueOf(json.getValue("value"));
                    }
                    return null;
                });
    }

    public Future<Void> setProperty(String key, String value, int expectedStatusCode)
    {
        JsonObject json = new JsonObject(Map.of("key", key, "value", value));
        return client
                .post(PORT, HOST, "/property")
                .timeout(2000)
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(json)
                .map(response ->
                {
                    int actualStatusCode = response.statusCode();
                    if (actualStatusCode != expectedStatusCode)
                        throw new RuntimeException(String.format("Unexpected status code. Expected: %d, Actual: %d %s",
                                expectedStatusCode, actualStatusCode, response.body()));
                    return null;
                });
    }
}
