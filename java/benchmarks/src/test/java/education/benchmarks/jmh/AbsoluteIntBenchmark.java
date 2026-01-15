package education.benchmarks.jmh;

import education.maths.Absolute;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class AbsoluteIntBenchmark
{
    private static final int MAX_METHOD_CALLS = 1000;

    private final Random random = new SecureRandom("Random".getBytes(StandardCharsets.UTF_8));

    @Benchmark
    public int Math_Abs() {
        int answer = 0;
        for(int x = 0; x < MAX_METHOD_CALLS; x++) {
            answer += Math.abs(random.nextInt());
        }
        return answer;
    }

    @Benchmark
    public int Absolute_abs() {
        int answer = 0;
        for(int x = 0; x < MAX_METHOD_CALLS; x++) {
            answer += Absolute.abs(random.nextInt());
        }
        return answer;
    }

    @Test
    public void launchBenchmark() throws RunnerException
    {
        Options opt = new OptionsBuilder()
                .include(this.getClass().getSimpleName())
                .warmupTime(TimeValue.seconds(5))
                .warmupIterations(2)
                .measurementTime(TimeValue.seconds(5))
                .measurementIterations(2)
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
