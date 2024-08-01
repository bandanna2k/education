package education.designpatterns.chainOfResponsibility;

public class ConsoleLogger extends AbstractLogger {

    public ConsoleLogger(int level)
    {
        this(level, null);
    }
    public ConsoleLogger(int level, AbstractLogger nextLogger)
    {
        super(nextLogger);
        this.level = level;
    }
    @Override
    protected void write(String message) {
        System.out.println("Standard Console::Logger: " + message);
    }
}