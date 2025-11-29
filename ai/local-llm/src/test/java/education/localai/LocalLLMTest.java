package education.localai;

import io.github.ollama4j.exceptions.OllamaException;
import io.github.ollama4j.models.generate.OllamaGenerateRequest;
import io.github.ollama4j.models.response.OllamaResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LocalLLMTest extends LocalLLMBase {

    @ParameterizedTest
    @ValueSource(strings = {
            "Question: What is my name? Answer:",
            "Context: My name is David. Question: What is my name? Answer:",
            "Question: What is the height of a tennis net in the middle? Answer:",
            "Question: How much do you know? Answer:",
            "Question: What should we have for dinner tonight? Answer:",
            "Question: Can you write a paragraph about the Mary Celeste? Answer:",
    })
    void shouldAnswerQuestions(final String prompt) throws OllamaException
    {
        OllamaGenerateRequest request = OllamaGenerateRequest.builder()
                .withModel("llama3.2")
                .withPrompt(prompt)
                .withOptions(seededOptions)
                .build();
        OllamaResult result = ollama.generate(request, null);
        System.out.println(result.getResponse());
    }

    @Test
    void answerOwnQuestion() throws OllamaException
    {
        String difficultQuestion = "Question: Give me the most difficult question you can think off, maximum 5 words? Answer:";
        String generatedDifficultQuestion = askQuestion(difficultQuestion);
        System.out.println(generatedDifficultQuestion);
        System.out.println(askQuestion(generatedDifficultQuestion));
    }
    private String askQuestion(String question) throws OllamaException
    {
        OllamaGenerateRequest request = OllamaGenerateRequest.builder()
                .withModel("llama3.2")
                .withPrompt(question)
                .withOptions(seededOptions)
                .build();
        OllamaResult result = ollama.generate(request, null);
        return result.getResponse();
    }

    @Test
    void shouldListModels() throws OllamaException
    {
        ollama.listModels().forEach(model -> System.out.println(model.getName()));
    }

    @Test
    void shouldGenerateImage() throws OllamaException
    {
        System.out.println(askQuestion("Generate a random jpg in Base64 encoding of a dog, 640 pixels by 480 pixels? Please verify that the answer can be decoded."));
    }
}
