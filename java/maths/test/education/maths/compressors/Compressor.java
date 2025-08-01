package education.maths.compressors;

import java.io.IOException;
import java.util.Set;

public interface Compressor {
    byte[] compress(Set<Integer> initialData) throws IOException;

    Set<Integer> inflate(byte[] compressed) throws IOException;
}
