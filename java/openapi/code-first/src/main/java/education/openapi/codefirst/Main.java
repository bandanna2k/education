package education.openapi.codefirst;

import education.openapi.codefirst.generator.SpecGenerator;
import io.swagger.v3.oas.models.OpenAPI;
import io.vertx.core.Vertx;

public class Main
{
    public static void main(String[] args)
    {
        OpenAPI openApi = new SpecGenerator().generate();

        Vertx vertx = Vertx.vertx();
        Application application = new Application(vertx);
        application.start(8080)
                .toCompletionStage()
                .toCompletableFuture()
                .join();
        System.out.println("Server started on port " + application.actualPort());
    }
}