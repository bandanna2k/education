package education.adventofcode.year2025.day2;

import education.adventofcode.year2025.TestBase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class Part2Test extends TestBase
{
    private static Stream<Arguments> part2() {
        return Stream.of(
                Arguments.of("/day2test.csv", 4174379265L),
                Arguments.of("/day2real.csv", 49046150754L)
        );
    }

    @ParameterizedTest(name = "Expected {1}")
    @MethodSource("part2")
    void part2(final String input, final long expected) throws IOException
    {
        Set<Long> invalidIds = new HashSet<>();

        try (InputStream inputStream = Part2Test.class.getResourceAsStream(input))
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            String[] ranges = line.split(",");
            for (String range : ranges)
            {
                String[] parts = range.split("-");
                String lower = parts[0];
                String higher = parts[1];

                long nLower = Long.parseLong(lower);
                long nUpper = Long.parseLong(higher);

//                System.out.println();
//                System.out.println(lower + " " + higher);
                for (long i = nLower; i <= nUpper; i++)
                {
                    if(isInvalid2(i)) {
                        invalidIds.add(i);
                    }
                }
            }
        }
        AtomicLong sumInvalidIds = new AtomicLong(0);
        invalidIds.forEach(sumInvalidIds::addAndGet);
        assertThat(sumInvalidIds.get()).isEqualTo(expected);
    }

    static int power(int base, int exp) {
        int result = 1;
        for (int i = 0; i < exp; i++) {
            result *= base;
        }
        return result;
    }

    private boolean isInvalid2(long nValue)
    {
        String value = String.valueOf(nValue);
        int length = value.length();
        int halfLength = length / 2;

        for (int repeaterSize = 1; repeaterSize <= halfLength; repeaterSize++)
        {
            if(length % repeaterSize != 0)
                continue;

            String repeater = value.substring(0, repeaterSize);
            String allRepeaters = "";
            for (int i = 0; i < length; i++)
            {
                allRepeaters += repeater;
            }
            allRepeaters = allRepeaters.substring(0, length);
            if(value.equals(allRepeaters))
            {
//                System.out.println(value + "   ->" + repeater);
                return true;
            }
        }
        return false;
    }
}
