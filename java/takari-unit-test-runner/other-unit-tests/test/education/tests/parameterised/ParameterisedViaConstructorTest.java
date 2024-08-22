package education.tests.parameterised;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParameterisedViaConstructorTest
{
    private final int inputNumber;
    private final boolean expectedResult;
    private final PrimeNumberChecker primeNumberChecker;

    // Each parameter should be placed as an argument here
    // Every time runner triggers, it will pass the arguments
    // from parameters we defined in primeNumbers() method

    public ParameterisedViaConstructorTest(int inputNumber, boolean expectedResult)
    {
        this.inputNumber = inputNumber;
        this.expectedResult = expectedResult;
        this.primeNumberChecker = new PrimeNumberChecker();
    }

    @Test
    public void testPrimeNumberChecker() {
        System.out.println("Parameterized Number is : " + inputNumber);
        assertEquals(expectedResult,
                primeNumberChecker.validate(inputNumber));
    }

    @Parameterized.Parameters
    public static Collection primeNumbers()
    {
        return Arrays.asList(new Object[][]{
                {2, true},
                {6, false},
                {19, true},
                {22, false},
                {23, true}
        });
    }

    private static class PrimeNumberChecker
    {
        public Boolean validate(final Integer primeNumber)
        {
            for (int i = 2; i < (primeNumber / 2); i++)
            {
                if (primeNumber % i == 0)
                {
                    return false;
                }
            }
            return true;
        }
    }
}
