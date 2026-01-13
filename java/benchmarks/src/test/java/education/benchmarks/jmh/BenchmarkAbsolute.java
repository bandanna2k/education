package education.benchmarks.jmh;

import org.openjdk.jmh.annotations.Benchmark;

public class BenchmarkAbsolute {

    @Benchmark
    public void init() {
        // Do nothing
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Here");
        org.openjdk.jmh.Main.main(args);
    }
}
