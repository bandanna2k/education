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

public class LocalLLMTest2 extends LocalLLMBase {

    @Test
    void shouldDescribeImage() throws OllamaException, IOException {

        File image = new File(LocalLLMTest2.class.getResource("/afghanistan.jpeg").getPath());
        OllamaGenerateRequest request = OllamaGenerateRequest.builder()
                .withModel("moondream:1.8b")
                .withImages(List.of(image))
                .withPrompt("Question: Describe this image and guess the country? Answer:")
                .withOptions(seededOptions)
                .build();
        OllamaResult result = ollama.generate(request, null);
        System.out.println(result.getResponse());
    }
}
