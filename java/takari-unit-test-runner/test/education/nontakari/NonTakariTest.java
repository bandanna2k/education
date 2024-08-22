package education.nontakari;

import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class NonTakariTest
{
    private final int exchangeRevision;
    private final int euclidRevision;
    private final Set<Class<?>> testClasses;

    public NonTakariTest(int exchangeRevision, int euclidRevision, Set<Class<?>> testClasses)
    {
        this.exchangeRevision = exchangeRevision;
        this.euclidRevision = euclidRevision;
        this.testClasses = testClasses;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> arguments()
    {
        Reflections reflections = new Reflections("education.tests", new SubTypesScanner(false));
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class)
                .stream()
                .filter(clazz -> clazz.getName().endsWith("Test"))
                .collect(Collectors.toSet());

        final List<Object[]> arguments = new ArrayList<>();
        for (int i = 0; i < 3; i++)
        {
            arguments.add(new Object[]
                    {
                            i, i, allClasses
                    });
        }
        return arguments;
    }

    @Test
    public void test()
    {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        testClasses.forEach(testClass -> {
            System.out.printf(
                    "Exchange: %d%n" +
                    "Euclid:   %d%n" +
                    "Test:     %s%n",
                    exchangeRevision, euclidRevision, testClass.getName()
            );
            Result result = junit.run(testClass);
            System.out.printf("=== %s ===%n", result.wasSuccessful() ? "SUCCESS" : "FAILED");
            System.out.println("=== Finished ===");
        });
    }
}
