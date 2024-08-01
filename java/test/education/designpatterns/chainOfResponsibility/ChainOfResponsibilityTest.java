package education.designpatterns.chainOfResponsibility;

import org.junit.Test;

public class ChainOfResponsibilityTest {

    @Test
    public void testChainOfResponsibility() {

        AbstractLogger loggerChain =
                new ErrorLogger(AbstractLogger.ERROR,
                        new FileLogger(AbstractLogger.DEBUG,
                                new ConsoleLogger(AbstractLogger.INFO)));

        loggerChain.logMessage(AbstractLogger.INFO, "This is an information.");
        loggerChain.logMessage(AbstractLogger.DEBUG, "This is an debug level information.");
        loggerChain.logMessage(AbstractLogger.ERROR, "This is an error information.");
    }
}
