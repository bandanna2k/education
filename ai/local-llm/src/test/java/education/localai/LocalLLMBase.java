package education.localai;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.github.ollama4j.Ollama;
import io.github.ollama4j.utils.Options;
import io.github.ollama4j.utils.OptionsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

class LocalLLMBase {

    private static GenericContainer<?> llmContainer;
    protected static Ollama ollama;
    protected static Options seededOptions;

    @BeforeAll
    static void beforeAll() {
        llmContainer = new GenericContainer<>(DockerImageName.parse("ollama-with-models"))
                .withCreateContainerCmdModifier(cmd -> cmd
                        .withName("ollama")
                        .withHostConfig(
                                new HostConfig().withPortBindings(
                                        new PortBinding(Ports.Binding.bindPort(11434), new ExposedPort(11434))))
                );
        llmContainer.start();

        ollama = new Ollama("http://localhost:11434/");
        ollama.setRequestTimeoutSeconds(10_000);

        int seed = 3;
        seededOptions = new OptionsBuilder()
                .setSeed(seed)
                .build();
    }

    @AfterAll
    static void afterAll()
    {
        llmContainer.stop();
    }
}