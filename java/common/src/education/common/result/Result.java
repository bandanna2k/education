package education.common.result;

import java.util.function.Consumer;

public class Result<S, E>
{
    private enum State
    {
        Success,
        Error
    }

    private final E errorData;
    private final S successData;
    private final State state;

    private Result(S success, E error, State state)
    {
        this.errorData = error;
        this.successData = success;
        this.state = state;
    }

    public static <S, E> Result<S, E> success(S data)
    {
        return new Result<>(data, null, State.Success);
    }

    public static <S, E> Result<S, E> failure(E data)
    {
        return new Result<>(null, data, State.Error);
    }

    public void fold(Consumer<S> successConsumer, Consumer<E> errorConsumer)
    {
        if(state == State.Success)
        {
            successConsumer.accept(successData);
        }
        else
        {
            errorConsumer.accept(errorData);
        }
    }

    public S success()
    {
        return successData;
    }

    @Override
    public String toString()
    {
        return state == State.Success ? "Success:" + successData : "Error:" + errorData;
    }
}