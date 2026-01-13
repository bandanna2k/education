package education.benchmarks.jmh;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import static education.FastHash.*;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class FastHashTest
{
    @Benchmark
    public long murmur()   {
        long answer = 0;
        for(long x = 0; x < 100000; x++) {
            answer += murmur64(x);
        }
        return answer;
    }

    @Benchmark
    public int murmur_32()   {
        int answer1 = 0;
        int answer2 = 0;
        for(long x = 0; x < 100000; x++) {
            long h = murmur64(x);
            answer1 += (int) h;
            answer2 += (int) (h >>> 32);
        }
        return answer1 + answer2;
    }

    @Benchmark
    public int fast2_32()   {
        int answer1 = 0;
        int answer2 = 0;

        for(long x = 0; x < 100000; x++) {
            answer1 += hash32_1(x);
            answer2 += hash32_2(x);

        }
        return answer1 + answer2;
    }
    @Benchmark
    public long fast64()   {
        long answer = 0;
        for(long x = 0; x < 100000; x++) {
            answer += hash64(x);
        }
        return answer;
    }

    @Test
    public void launchBenchmark() throws RunnerException
    {
        Options opt = new OptionsBuilder()
                .include(FastHashTest.class.getSimpleName()).warmupIterations(5)
                .measurementIterations(10).forks(1).build();
        new Runner(opt).run();
    }
}
