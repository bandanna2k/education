package education.localai.guardrails;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainerLocalLLM implements AutoCloseable
{
    private final GenericContainer<?> llmContainer;

    public TestContainerLocalLLM()
    {
        llmContainer = new GenericContainer<>(DockerImageName.parse("ollama-with-models"))
                .withCreateContainerCmdModifier(cmd -> cmd
                        .withName("ollama")
                        .withHostConfig(
                                new HostConfig().withPortBindings(
                                        new PortBinding(Ports.Binding.bindPort(11434), new ExposedPort(11434))))
                );
        llmContainer.start();
    }

    @Override
    public void close()
    {
        llmContainer.stop();
        llmContainer.close();
    }
}
