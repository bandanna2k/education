package education.openapi.codefirst;

import io.vertx.core.Vertx;

public class Main
{
    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();
        Application application = new Application(vertx);
        application.start(8080)
                .toCompletionStage()
                .toCompletableFuture()
                .join();
        System.out.println("Server started on port " + application.actualPort());
    }
}