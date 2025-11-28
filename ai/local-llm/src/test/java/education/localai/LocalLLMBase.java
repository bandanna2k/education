package education.localai;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

class LocalLLMBase {

    private static GenericContainer<?> llmContainer;

    @BeforeAll
    static void beforeAll() {
        llmContainer = new GenericContainer<>(DockerImageName.parse("mysql"))
                .withCreateContainerCmdModifier(cmd -> cmd
                        .withName("ollama")
                        .withHostConfig(
                                new HostConfig().withPortBindings(
                                        new PortBinding(Ports.Binding.bindPort(11434), new ExposedPort(11434))))
                );
    }

    @AfterEach
    void tearDown() {
        llmContainer.stop();
    }
}