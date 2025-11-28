package education.localai;

import io.vertx.core.Vertx;

import java.io.IOException;

public class LocalLLM {

    private QueryService queryService;

    public void init(String path, String ollamaUrl) throws IOException {
        DocumentLoader documentLoader = new DocumentLoader();
        documentLoader.loadFromDirectory(path);

        OllamaClient ollamaClient = new OllamaClient(Vertx.vertx(), ollamaUrl);

        queryService = new QueryService(documentLoader, ollamaClient, 10_000_000);
    }

    public void query(String s) {
        queryService.query(s);
    }
}
