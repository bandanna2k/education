package education.localai.guardrails;

import java.util.Optional;

public final class Answer
{
    private final String question;
    private final String answerUnguarded;
    private final String inputGuardError;
    private final String outputGuardError;

    public Answer(
            String question,
            String answerUnguarded,
            String inputGuardError,
            String outputGuardError)
    {
        this.question = question;
        this.answerUnguarded = answerUnguarded;
        this.inputGuardError = inputGuardError;
        this.outputGuardError = outputGuardError;
    }

    public String question()
    {
        return question;
    }

    public String getAnswer()
    {
        if (inputGuardError != null)
        {
            return inputGuardError;
        }
        if (outputGuardError != null)
        {
            return outputGuardError;
        }
        return answerUnguarded;
    }

    public Optional<String> getInputGuardMessage()
    {
        return Optional.ofNullable(inputGuardError);
    }

    public Optional<String> getOutputGuardMessage()
    {
        return Optional.ofNullable(outputGuardError);
    }

    public static class Builder
    {
        private String question;
        private String answerUnguarded;
        private String inputGuardError;
        private String outputGuardError;

        Answer build()
        {
            return new Answer(question, answerUnguarded, inputGuardError, outputGuardError);
        }

        public Builder question(String question)
        {
            this.question = question;
            return this;
        }

        public Builder answerUnguarded(String answerUnguarded)
        {
            this.answerUnguarded = answerUnguarded;
            return this;
        }

        public String answerUnguarded()
        {
            return answerUnguarded;
        }

        public Builder inputGuardError(String inputGuardError)
        {
            this.inputGuardError = inputGuardError;
            return this;
        }

        public Builder outputGuardError(String outputGuardError)
        {
            this.outputGuardError = outputGuardError;
            return this;
        }
    }
}
