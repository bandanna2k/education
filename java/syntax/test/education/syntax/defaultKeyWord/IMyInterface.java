package education.syntax.defaultKeyWord;

public interface IMyInterface
{
    default int getDefaultValue()
    {
        return Integer.MAX_VALUE;
    }
}
