package education.openapi;

import education.openapi.codegen.GenerateClasses;
import io.vertx.core.Vertx;

public class Main
{
    public static void main(String[] args)
    {
        new GenerateClasses().go();

        Vertx vertx = Vertx.vertx();
        Application application = new Application(vertx);
        application.start(8080)
                .toCompletionStage()
                .toCompletableFuture()
                .join();
        System.out.println("Server started on port " + application.actualPort());
    }
}