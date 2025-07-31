package education.maths;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class SortedIntegers {
    final Set<Integer> initialData = initialData();

    private Set<Integer> initialData() {
        TreeSet<Integer> integers = new TreeSet<>();
        for (int i = 0; i < 100; i++) {
            integers.add(i);
        }
        for (int i = 1000; i < 1100; i++) {
            integers.add(i);
        }
        return integers;
    }

    @Test
    public void inputShouldMatchOutput() throws IOException {
        Compressor compressor = new NoCompression();
        byte[] compressed = compressor.compress(initialData);
        String base64compressed = Base64.getEncoder().encodeToString(compressed);
        System.out.printf("(%d) %s%n", base64compressed.length(), base64compressed);
        Set<Integer> uncompressed = compressor.inflate(compressed);
        Assertions.assertThat(uncompressed).containsExactlyInAnyOrder(initialData.toArray(Integer[]::new));
    }

    private interface Compressor {
        byte[] compress(Set<Integer> initialData) throws IOException;

        Set<Integer> inflate(byte[] compressed) throws IOException;
    }

    private static class NoCompression implements Compressor {

        byte[] inflateBuffer = new byte[4];

        @Override
        public byte[] compress(Set<Integer> initialData) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (Integer n : initialData) {
                baos.write(intToByteArray(n));
            }
            return baos.toByteArray();
        }

        @Override
        public Set<Integer> inflate(byte[] compressed) throws IOException {
            Set<Integer> result = new HashSet<>();
            try(ByteArrayInputStream bais = new ByteArrayInputStream(compressed)) {
                while ((inflateBuffer[0] = (byte)bais.read()) != -1) {
                    inflateBuffer[1] = (byte)bais.read();
                    inflateBuffer[2] = (byte)bais.read();
                    inflateBuffer[3] = (byte)bais.read();
                    result.add(bytesToInt(inflateBuffer));
                }
            }
            return result;
        }
    }

    public static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value};
    }

    public static int bytesToInt(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }
}
