package education.text2image;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

class TextToImageBase {

    private static final GenericContainer<?> text2imageContainer;

    static
    {
        text2imageContainer = new GenericContainer<>(DockerImageName.parse("text-to-image"))
                .withCreateContainerCmdModifier(cmd -> cmd
                        .withName("text-to-image")
                        .withHostConfig(
                                new HostConfig().withPortBindings(
                                        new PortBinding(Ports.Binding.bindPort(5000), new ExposedPort(5000))))
                );
        text2imageContainer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            text2imageContainer.stop();
            System.out.println("Shutting down text2image");
        }));

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}