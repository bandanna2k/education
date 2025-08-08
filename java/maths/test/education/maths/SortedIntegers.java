package education.maths;

import education.maths.compressors.Compressor;
import education.maths.compressors.FastPFORCompression;
import education.maths.compressors.LzmaCompression;
import education.maths.compressors.NoCompression;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class SortedIntegers {
    final static Set<Integer> INITIAL_DATA = initialData();

    private static Set<Integer> initialData() {
        TreeSet<Integer> integers = new TreeSet<>();
        for (int i = 0; i < 100; i++) {
            integers.add(i);
        }
        for (int i = 1000; i < 1100; i++) {
            integers.add(i);
        }
        return integers;
    }

    private final List<Compressor> compressors = List.of(
        new NoCompression(),
        new FastPFORCompression()
        //new LzmaCompression()
    );

    @Test
    public void inputShouldMatchOutput() throws IOException {
        for (Compressor compressor : compressors) {
            inputShouldMatchOutput(compressor);
        }
    }
    public void inputShouldMatchOutput(final Compressor compressor) throws IOException {
        System.out.println("Testing: " + compressor.getClass().getSimpleName());
        byte[] compressed = compressor.compress(INITIAL_DATA);
        String base64compressed = Base64.getEncoder().encodeToString(compressed);
        System.out.printf("(%d) %s%n", base64compressed.length(), base64compressed);
        Set<Integer> uncompressed = compressor.inflate(compressed);
        Assertions.assertThat(uncompressed).containsExactlyInAnyOrder(INITIAL_DATA.toArray(Integer[]::new));
    }
}
