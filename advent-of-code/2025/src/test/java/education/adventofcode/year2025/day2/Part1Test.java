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

import static java.lang.Math.floorMod;
import static org.assertj.core.api.Assertions.assertThat;

public class Part1Test extends TestBase
{
    private static Stream<Arguments> part1() {
        return Stream.of(
                Arguments.of("/day2test.csv", 1227775554),
                Arguments.of("/day2real.csv", 38437576669L)
        );
    }

    @ParameterizedTest(name = "Expected {1}")
    @MethodSource("part1")
    void part1(final String input, final long expected) throws IOException
    {
        int invalidIds = 0;
        long sumInvalidIds = 0;
        try (InputStream inputStream = Part1Test.class.getResourceAsStream(input))
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

                for (long i = nLower; i <= nUpper; i++)
                {
                    if(isInvalid(i)) {
                        invalidIds++;
                        sumInvalidIds += i;
                    }
                }
            }
        }
        assertThat(sumInvalidIds).isEqualTo(expected);
    }

    private boolean isInvalid(long nValue)
    {
        String value = String.valueOf(nValue);
        int length = value.length();
        if(length % 2 == 1) return false;
        int mid = length / 2;
        for (int i = 0; i < mid; i++)
        {
            if(value.charAt(i) != value.charAt(i + mid)) return false;
        }
//        System.out.println(nValue);
        return true;
    }
}
