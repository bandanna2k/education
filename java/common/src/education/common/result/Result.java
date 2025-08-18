package education.common.result;

import java.util.function.Consumer;
import java.util.function.Function;

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

    public void consume(Consumer<S> successConsumer, Consumer<E> errorConsumer)
    {
        if(isSuccess())
        {
            successConsumer.accept(successData);
        }
        else
        {
            errorConsumer.accept(errorData);
        }
    }

    public <NewS, NewE> Result<NewS, NewE> map(Function<S, NewS> successFunction, Function<E, NewE> errorFunction)
    {
        if(isSuccess())
        {
            return success(successFunction.apply(successData));
        }
        else
        {
            return failure(errorFunction.apply(errorData));
        }
    }

    public <NewS> Result<NewS, E> map(Function<S, NewS> successFunction)
    {
        if(isSuccess())
        {
            return success(successFunction.apply(successData));
        }
        else
        {
            return failure(errorData);
        }
    }

    public <NewE> Result<S, NewE> mapError(Function<E, NewE> errorFunction)
    {
        if(isSuccess())
        {
            return success(successData);
        }
        else
        {
            return failure(errorFunction.apply(errorData));
        }
    }

    //    public <N> Result<N, E> map(Mapping<S, N, E> result)
//    {
//        if(hasFailed())
//        {
//            return failure(errorData);
//        }
//        return result.map(success());
//    }
//
    public S success()
    {
        assert isSuccess() : "Result not successful";
        return successData;
    }

    public E error()
    {
        assert hasFailed() : "Result not failed";
        return errorData;
    }

    public boolean isSuccess()
    {
        return state == State.Success;
    }

    public boolean hasFailed()
    {
        return state == State.Error;
    }

    @Override
    public String toString()
    {
        return isSuccess() ? "Success:" + successData : "Error:" + errorData;
    }

    @FunctionalInterface
    public interface Mapping<S, N, E>
    {
        Result<N, E> map(S success);
    }
}