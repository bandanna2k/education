package education.localai;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.net.URI;
import java.net.http.HttpRequest;

public class OllamaClient {
    private static final String MODEL = "llama3.2";

    private final String ollamaUrl;
    private final WebClient client;

    public OllamaClient(Vertx vertx, String ollamaUrl) {
        this.ollamaUrl = ollamaUrl;
        this.client = WebClient.create(vertx);
    }

    public Future<JsonObject> query(String prompt) {
        JsonObject requestBody = new JsonObject()
                .put("model", MODEL)
                .put("prompt", prompt)
                .put("stream", false);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ollamaUrl + "/api/generate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.encode()))
                .build();

        return client.post(ollamaUrl, "/api/generate")
                .putHeader("Content-Type", "application/json")
                .send()
                .onSuccess(response -> {
                    if (response.statusCode() != 200) {
                        throw new RuntimeException("Error sending request. " + response);
                    }
                })
                .map(HttpResponse::bodyAsJsonObject);
    }
}
