package education.localai;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.github.ollama4j.Ollama;
import io.github.ollama4j.exceptions.OllamaException;
import io.github.ollama4j.models.generate.OllamaGenerateRequest;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.Options;
import io.github.ollama4j.utils.OptionsBuilder;
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
Question: What is my name?
Answer:""";
    private static final String QUESTION2 = """
Question: What is the height of a tennis net in the middle?
Answer:""";
    private static final String QUESTION3 = """
Question: Give me the most difficult question you can think off, maximum 5 words?
Answer:""";
    private static final String QUESTION4 = """
Question: What should we have for dinner tonight?
Answer:""";


    @ParameterizedTest
    @ValueSource(strings = {
            QUESTION1,
            "Context: My name is David. " + QUESTION1,
            QUESTION2,
            QUESTION3,
            QUESTION4
    })
    void shouldGetHeightOfTheTennisNet(final String prompt) throws OllamaException
    {
        int seed = 1;

        Ollama ollama = new Ollama("http://localhost:11434/");
        ollama.setRequestTimeoutSeconds(10_000);

        ollama.listModels().forEach(model -> System.out.println(model.getName()));

        Options options = new OptionsBuilder()
                .setSeed(seed)
                .build();
        OllamaGenerateRequest request = OllamaGenerateRequest.builder()
                .withModel("llama3.2")
                .withPrompt(prompt)
                .withOptions(options)
                .build();
        OllamaResult result = ollama.generate(request, null);
        System.out.println(result.getResponse());
    }
}
