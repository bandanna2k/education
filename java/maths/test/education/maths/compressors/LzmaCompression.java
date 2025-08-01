package education.maths.compressors;

import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.LZMAInputStream;
import org.tukaani.xz.LZMAOutputStream;
import org.tukaani.xz.UnsupportedOptionsException;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static education.maths.bytes.ByteArrayHelper.bytesToInt;
import static education.maths.bytes.ByteArrayHelper.intToByteArray;

public class LzmaCompression implements Compressor {

    private LZMA2Options lzma2Options;
    private byte[] inflateBuffer = new byte[4];

    public LzmaCompression() {
        try {
            lzma2Options = new LZMA2Options(9);
        } catch (UnsupportedOptionsException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] compress(Set<Integer> initialData) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             LZMAOutputStream lzos = new LZMAOutputStream(baos, lzma2Options, true)) {
            for (Integer n : initialData) {
                lzos.write(intToByteArray(n));
            }
            lzos.finish();
            return baos.toByteArray();
        }
    }

    @Override
    public Set<Integer> inflate(byte[] compressed) throws IOException {
        Set<Integer> result = new HashSet<>();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressed);
             BufferedInputStream bis = new BufferedInputStream(bais);
             InputStream lzis = new LZMAInputStream(bis)) {
            while (0 != lzis.available()) {
                int read = lzis.read(inflateBuffer);
                assert 4 == read;
                result.add(bytesToInt(inflateBuffer));
            }
        }
        return result;
    }
}
