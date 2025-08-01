package education.maths.compressors;

import education.maths.SortedIntegers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static education.maths.bytes.ByteArrayHelper.bytesToInt;
import static education.maths.bytes.ByteArrayHelper.intToByteArray;

public class NoCompression implements Compressor {

    byte[] inflateBuffer = new byte[4];

    @Override
    public byte[] compress(Set<Integer> initialData) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            for (Integer n : initialData) {
                baos.write(intToByteArray(n));
            }
            return baos.toByteArray();
        }
    }

    @Override
    public Set<Integer> inflate(byte[] compressed) throws IOException {
        Set<Integer> result = new HashSet<>();
        try (InputStream bais = new ByteArrayInputStream(compressed)) {
            while ((inflateBuffer[0] = (byte) bais.read()) != -1) {
                inflateBuffer[1] = (byte) bais.read();
                inflateBuffer[2] = (byte) bais.read();
                inflateBuffer[3] = (byte) bais.read();
                result.add(bytesToInt(inflateBuffer));
            }
        }
        return result;
    }
}
