package education.localai;

import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryService {
    private final DocumentLoader documentLoader;
    private final OllamaClient ollamaClient;
    //    private final OllamaClient ollamaClient;
    private final int maxContentLength;
    private final String allDocsContext;

    public QueryService(DocumentLoader documentLoader, OllamaClient ollamaClient, int maxContentLength) {
        this.documentLoader = documentLoader;
        this.ollamaClient = ollamaClient;
        this.maxContentLength = maxContentLength;

        // Pre-build context with all documents
        this.allDocsContext = buildAllDocsContext();
        System.out.println("Loaded context: " + allDocsContext.length() + " characters");
    }

    /**
     * Build a single context string with all documents
     * TODO Move this method out
     */
    private String buildAllDocsContext() {
        StringBuilder context = new StringBuilder();

        for (Map.Entry<String, String> entry : documentLoader.getDocuments().entrySet()) {
            String filename = entry.getKey();
            String content = entry.getValue();

            context.append("\n\n=== DOCUMENT: ").append(filename).append(" ===\n");
            context.append(content);
        }

        return truncateText(context.toString());
    }

    /**
     * Query against all loaded documents
     */
    public Future<QueryResult> query(String question) {
        String prompt = buildPrompt(question);

        return ollamaClient.query(prompt)
                .map(answer -> {
                    System.out.println(answer.toString());
                    return new QueryResult(
                            new ArrayList<>(documentLoader.getFilenames()),
                            question,
                            answer.getString("answer"),
                            allDocsContext.length()
                    );
                });
    }

    private String truncateText(String text) {
        if (text.length() <= maxContentLength) {
            return text;
        }
        System.out.println("WARNING: Context truncated from " + text.length() + " to " + maxContentLength + " chars");
        return text.substring(0, maxContentLength) + "\n\n... (content truncated due to size)";
    }

    private String buildPrompt(String question) {
        return String.format(
                "You have access to the following documents. Answer the question based on the information in these documents.\n\n" +
                        "%s\n\n" +
                        "Question: %s\n\n" +
                        "Answer (cite which document(s) you reference):",
                allDocsContext,
                question
        );
    }

    public String getContextPreview() {
        return allDocsContext.length() > 500
                ? allDocsContext.substring(0, 500) + "..."
                : allDocsContext;
    }

    public int getContextSize() {
        return allDocsContext.length();
    }

    public static class QueryResult {
        private final List<String> filenames;
        private final String question;
        private final String answer;
        private final int contextLength;

        public QueryResult(List<String> filenames, String question, String answer, int contextLength) {
            this.filenames = filenames;
            this.question = question;
            this.answer = answer;
            this.contextLength = contextLength;
        }

        public List<String> getFilenames() { return filenames; }
        public String getQuestion() { return question; }
        public String getAnswer() { return answer; }
        public int getContextLength() { return contextLength; }
    }
}