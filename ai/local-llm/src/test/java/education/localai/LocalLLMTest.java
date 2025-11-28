package education.localai;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.github.ollama4j.Ollama;
import io.github.ollama4j.exceptions.OllamaException;
import io.github.ollama4j.models.generate.OllamaGenerateRequest;
import io.github.ollama4j.models.response.OllamaResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

public class LocalLLMTest extends LocalLLMBase {

    private static final String QUESTION1 = """
My name is David.

Question: What is my name?
Answer:""";
    private static final String QUESTION2 = """
Question: What is the height of a tennis net in the middle?
Answer:""";


    @ParameterizedTest
    @ValueSource(strings = {
            QUESTION1,
            QUESTION2
    })
    void shouldGetHeightOfTheTennisNet(final String prompt) throws OllamaException
    {
        Ollama ollama = new Ollama("http://localhost:11434/");
        ollama.listModels().forEach(model -> System.out.println(model.getName()));
        OllamaGenerateRequest request = OllamaGenerateRequest.builder()
                .withModel("llama3.2")
                .withPrompt(prompt)
                .build();
        OllamaResult result = ollama.generate(request, null);
        System.out.println(result.getResponse());
    }
}
