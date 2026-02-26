package education.designpatterns.chainOfResponsibility;

public class FileLogger extends AbstractLogger {

    public FileLogger(int level)
    {
        this(level, null);
    }
    public FileLogger(int level, AbstractLogger nextLogger)
    {
        super(nextLogger);
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("File::Logger: " + message);
    }
}