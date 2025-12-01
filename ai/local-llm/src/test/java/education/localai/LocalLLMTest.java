package education.localai;

import io.github.ollama4j.exceptions.OllamaException;
import io.github.ollama4j.models.generate.OllamaGenerateRequest;
import io.github.ollama4j.models.response.OllamaResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LocalLLMTest extends LocalLLMBase {

    @ParameterizedTest
    @ValueSource(strings = {
            "Question: What is my name? Answer:",
            "Context: My name is David. Question: What is my name? Answer:",
            "Question: What is the height of a tennis net in the middle? Answer:",
            "Question: How much do you know? Answer:",
            "Question: What should we have for dinner tonight? Answer:",
            "Question: Can you write a paragraph about the Mary Celeste? Answer:",
            "Question: Describe a fictitious animal? Answer:",
            "Question: I have an apple, a mango, an egg and some flour, create me a recipe? Answer:",
            "Question: I have banana, sugar, bread dough, flour and capsicum, create me a recipe? Answer:",
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

    @ParameterizedTest
    @ValueSource(strings = {
            "/afghanistan.jpeg",
            "/gore.sign.jpg",
    })
    void shouldDescribeImage(final String resource) throws OllamaException, IOException
    {
        File image = new File(LocalLLMTest.class.getResource(resource).getPath());
        {
            OllamaGenerateRequest request = OllamaGenerateRequest.builder()
                    .withModel("moondream:1.8b")
                    .withImages(List.of(image))
                    .withPrompt("Question: Describe this image? Answer:")
                    .withOptions(seededOptions)
                    .build();
            OllamaResult result = ollama.generate(request, null);
            System.out.println(request.getPrompt());
            System.out.println(result.getResponse());
        }
        {
            OllamaGenerateRequest request = OllamaGenerateRequest.builder()
                    .withModel("moondream:1.8b")
                    .withImages(List.of(image))
                    .withPrompt("Question: Guess the country from the image? Answer:")
                    .withOptions(seededOptions)
                    .build();
            OllamaResult result = ollama.generate(request, null);
            System.out.println(request.getPrompt());
            System.out.println(result.getResponse());
        }
    }
}
