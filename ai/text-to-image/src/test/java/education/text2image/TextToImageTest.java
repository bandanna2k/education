package education.text2image;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;

class TextToImageTest extends TextToImageBase {

    @Test
    void shouldCreateImage() {
        Vertx vertx = Vertx.vertx();
        WebClient client = WebClient.create(vertx);

        String outputPath = String.format("/tmp/downloaded-image-%s.png", Instant.now());
        Prompt prompt1 = new Prompt("Woman sitting on rock by stream painting — serene nature scene artwork");
        Prompt prompt = new Prompt("A team of young mystery solvers and their talking dog , cartoon line art , clean outlines, retro vibe");

        Future<HttpResponse<Buffer>> future = client.post(5000, "localhost", "/generate")
                .sendJson(prompt)
                .onSuccess(response -> {
                    if (response.statusCode() == 200) {
                        Buffer imageBuffer = response.body();

                        vertx.fileSystem().writeFile(outputPath, imageBuffer)
                                .onSuccess(v -> {
                                    System.out.println("Downloaded: " + outputPath);
                                    System.out.println("Downloaded: " + imageBuffer.length() + " bytes");
                                    vertx.close();
                                })
                                .onFailure(err -> {
                                    System.err.println("Save failed: " + err.getMessage());
                                    vertx.close();
                                });
                    } else {
                        System.err.println("HTTP " + response.statusCode());
                    }
                })
                .onFailure(err -> {
                    System.err.println("Request failed: " + err.getMessage());
                })
                .onComplete(bufferHttpResponse -> {
                    vertx.close();
                });
        future.toCompletionStage().toCompletableFuture().join();
    }

    public static class Prompt {
        public final String prompt;
        public final int width = 768;
        public final int height = 1024;
        public final int steps = 30;
        public final String guidance_scale = "7.5";

        public Prompt(String prompt) {
            this.prompt = prompt;
        }
    }
}