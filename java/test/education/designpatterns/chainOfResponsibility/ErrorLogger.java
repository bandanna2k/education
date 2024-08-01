package education.designpatterns.chainOfResponsibility;

public class ErrorLogger extends AbstractLogger {

    public ErrorLogger(int level)
    {
        this(level, null);
    }
    public ErrorLogger(int level, AbstractLogger nextLogger)
    {
        super(nextLogger);
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("Error Console::Logger: " + message);
    }
}