package education.localai.guardrails;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import education.common.result.Result;
import io.github.ollama4j.Ollama;
import io.github.ollama4j.exceptions.OllamaException;
import io.github.ollama4j.models.generate.OllamaGenerateRequest;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.Options;
import io.github.ollama4j.utils.OptionsBuilder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static education.common.result.Result.success;

public class LocalLLM implements AutoCloseable
{
    private final String GUARD_MODEL = "llama-guard3:8b";
    private final String MAIN_MODEL = "llama3.2";

    private GenericContainer<?> llmContainer;
    private Ollama ollama;
    private Options seededOptions;

    public LocalLLM()
    {
        llmContainer = new GenericContainer<>(DockerImageName.parse("ollama-with-models"))
                .withCreateContainerCmdModifier(cmd -> cmd
                        .withName("ollama")
                        .withHostConfig(
                                new HostConfig().withPortBindings(
                                        new PortBinding(Ports.Binding.bindPort(11434), new ExposedPort(11434))))
                );
        llmContainer.start();

        ollama = new Ollama("http://localhost:11434/");
        ollama.setRequestTimeoutSeconds(30_000);

        int seed = 3;
        seededOptions = new OptionsBuilder()
                .setSeed(seed)
                .build();
    }

    @Override
    public void close()
    {
        llmContainer.stop();
        llmContainer.close();
    }

    public Result<String, String> ask(String question)
    {
        Result<Void, String> resultInputSafety = checkInputSafety(question);
        return resultInputSafety.map(
                s -> {
                    return askWithMainModel(question).success();
                },
                error -> error);
    }

    Result<String, String> askWithMainModel(String userInput) {
        String prompt = "User: " + userInput + "\n\nAssistant:";

        OllamaGenerateRequest request = OllamaGenerateRequest.builder()
                .withModel(MAIN_MODEL)
                .withPrompt(prompt)
                .withOptions(seededOptions)
                .build();
        try
        {
            OllamaResult ollamaResult = ollama.generate(request, null);
            return success(ollamaResult.getResponse().trim());
        }
        catch (OllamaException e)
        {
            return Result.failure(e.getMessage());
        }
    }

    // Check if input is safe
    public Result<Void, String> checkInputSafety(String userInput)
    {
        String guardPrompt = buildGuardPrompt("User", userInput, Optional.empty());
        return evaluateWithGuard(guardPrompt);
    }

    // Check if output is safe
    public Result<Void, String> checkOutputSafety(String userInput, String llmOutput)
    {
        String guardPrompt = buildGuardPrompt("Agent", userInput, Optional.of(llmOutput));
        return evaluateWithGuard(guardPrompt);
    }

    private Result<Void, String> evaluateWithGuard(String guardPrompt)
    {
        try
        {
            OllamaGenerateRequest request = OllamaGenerateRequest.builder()
                    .withModel(GUARD_MODEL)
                    .withPrompt(guardPrompt)
                    .withOptions(seededOptions)
                    .build();

            OllamaResult result = ollama.generate(request, null);
            String answer = result.getResponse().toLowerCase();

            if (answer.contains("safe") && !answer.contains("unsafe"))
            {
                return success(null);
            }
            else
            {
                return Result.failure(answer);
            }
        }
        catch (OllamaException e)
        {
            return Result.failure(e.getMessage());
        }
    }

    private static final String[] UNSAFE_CATEGORIES = {
            "S1: Violent Crimes",
            "S2: Non-Violent Crimes",
            "S3: Sex Crimes",
            "S4: Child Exploitation",
            "S5: Defamation",
            "S6: Specialized Advice",
            "S7: Privacy",
            "S8: Intellectual Property",
            "S9: Indiscriminate Weapons",
            "S10: Hate",
            "S11: Self-Harm",
            "S12: Sexual Content",
            "S13: Elections",
            "S14: Code Interpreter Abuse"
    };

    private String buildGuardPrompt(String role,
                                    String userInput,
                                    Optional<String> maybeAgentOutput)
    {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Task: Check if there is unsafe content in ")
                .append(role.toLowerCase())
                .append(" messages in conversations according to our safety policy.\n\n");

        prompt.append("<BEGIN UNSAFE CONTENT CATEGORIES>\n");
        for (String category : UNSAFE_CATEGORIES)
        {
            prompt.append(category).append("\n");
        }
        prompt.append("<END UNSAFE CONTENT CATEGORIES>\n\n");

        prompt.append("<BEGIN CONVERSATION>\n");
        prompt.append("User: ").append(userInput).append("\n");

        maybeAgentOutput.ifPresent(agentOutput ->
                prompt.append("Agent: ").append(agentOutput).append("\n"));

        prompt.append("<END CONVERSATION>\n\n");

        prompt.append("Determine if the ").append(role.toLowerCase())
                .append(" message is safe or unsafe. If unsafe, identify the categories violated.\n")
                .append("Response format: [SAFE] or [UNSAFE] followed by violated categories.");

        return prompt.toString();
    }
}
