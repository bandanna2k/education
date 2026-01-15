package education.benchmarks.jmh;

import education.benchmarks.jmh.common.BenchmarkBase;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Thread)
public class ParamBenchmark extends BenchmarkBase
{
    @Param({ "10", "20", "30"})
    public int arg;

    @State(Scope.Thread)
    public static class MyState
    {
        public int a = 1;
        public int b = 2;
        public int c;
        public int sum;
    }

    @Benchmark
    public void testMethod(MyState state, Blackhole bh)
    {
        state.sum = state.a + state.b + arg;
        bh.consume(state.sum);
    }
}
