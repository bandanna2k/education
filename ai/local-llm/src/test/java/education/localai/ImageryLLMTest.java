package education.localai;

import io.github.ollama4j.exceptions.OllamaException;
import io.github.ollama4j.models.generate.OllamaGenerateRequest;
import io.github.ollama4j.models.response.OllamaResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageryLLMTest extends LocalLLMBase
{
    @ParameterizedTest
    @ValueSource(strings = {
            "/afghanistan.jpeg",
            "/gore.sign.jpg",
    })
    void shouldDescribeImage(final String resource) throws OllamaException, IOException
    {
        File image = new File(LLMAdapterTest.class.getResource(resource).getPath());
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
