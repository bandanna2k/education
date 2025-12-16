package education.localai.guardrails;

import education.common.result.Result;
import io.github.ollama4j.Ollama;
import io.github.ollama4j.exceptions.OllamaException;
import io.github.ollama4j.models.generate.OllamaGenerateRequest;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.Options;
import io.github.ollama4j.utils.OptionsBuilder;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static education.common.result.Result.failure;
import static education.common.result.Result.success;

public class LLM_Client
{
    private static final String GUARD_MODEL = "llama-guard3:8b";
    private static final String GUARD_MODEL_FAST = "llama-guard3:1b";

    private final Ollama ollama;
    private final Options seededOptions;
    private final String model;

    public LLM_Client(String model)
    {
        this.model = model;
        ollama = new Ollama("http://localhost:11434/");
        ollama.setRequestTimeoutSeconds(30_000);

        int seed = 4;
        seededOptions = new OptionsBuilder()
                .setSeed(seed)
                .build();
    }

    public Result<Answer, String> ask(String question)
    {
        Answer.Builder builder = new Answer.Builder();
        builder.question(question);

        Result<Void, String> resultInputSafety = checkInputSafety(question);
        resultInputSafety.ifError(builder::inputGuardError);

        Result<String, String> resultWithMainModel = askWithMainModel(question);
        if(resultWithMainModel.isSuccess())
        {
            builder.answerUnguarded(resultWithMainModel.success());
        }
        else
        {
            return failure(resultWithMainModel.error());
        }

        Result<Void, String> resultOutputSafety = checkOutputSafety(question, builder.answerUnguarded());
        resultOutputSafety.ifError(builder::outputGuardError);

        return success(builder.build());
    }

    private Result<String, String> askWithMainModel(String userInput) {
        String prompt = "User: " + userInput + "\n\nAssistant:";

        OllamaGenerateRequest request = OllamaGenerateRequest.builder()
                .withModel(model)
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
        return evaluateWithGuard(guardPrompt).mapError(error ->
                String.join(",", error.split("[ |\\n]")));
    }

    // Check if output is safe
    public Result<Void, String> checkOutputSafety(String userInput, String llmOutput)
    {
        String guardPrompt = buildGuardPrompt("Agent", userInput, Optional.of(llmOutput));
        return evaluateWithGuard(guardPrompt).mapError(error ->
                String.join(",", error.split("[ |\\n]")));
    }

    private Result<Void, String> evaluateWithGuard(String guardPrompt)
    {
        try
        {
            OllamaGenerateRequest request = OllamaGenerateRequest.builder()
                    .withModel(GUARD_MODEL_FAST)
                    .withPrompt(guardPrompt)
                    .withOptions(seededOptions)
                    .build();

            OllamaResult result = ollama.generate(request, null);
            String answerGuarded = result.getResponse().toLowerCase();

            if (answerGuarded.contains("safe") && !answerGuarded.contains("unsafe"))
            {
                return success(null);
            }
            else
            {
                return Result.failure(answerGuarded);
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
