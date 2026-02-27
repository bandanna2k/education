package education.onebrc.memorymapping;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileReadingBytesTest {

    public static final int EXPECTED = -261835294;

    @Test
    void readWithRandomAccessFileOnly() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(FILE, "r")) {
            int sum = 0;
            int input;
            while((input = file.read()) != -1) {
                sum += input;
            }
            assertThat(sum).isEqualTo(EXPECTED);
            System.out.println(sum);
        }
    }

    @Test @Order(2)
    void readWithMemoryMapping() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(FILE, "r");
             FileChannel channel = file.getChannel()) {

            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            int sum = 0;
            while (buffer.hasRemaining()) {
                sum += (buffer.get() & 0xFF);
            }
            assertThat(sum).isEqualTo(EXPECTED);
            System.out.println(sum);
        }
    }

    @Test
    void readWithLongNativeHandler() throws IOException {
        byte[] bytes = Files.readAllBytes(FILE.toPath());
        int fileSize = bytes.length;

        int i = 0;
        int sum = 0;
        for (; i < fileSize; i++) {
            sum += ((byte) BYTE_HANDLE.get(bytes, i) & 0xFF);
        }

        assertThat(sum).isEqualTo(EXPECTED);
        System.out.println(sum);
    }

    @BeforeAll
    public static void needsXmx16g()
    {
        long maxMemoryBytes = Runtime.getRuntime().maxMemory();
        long maxMemMb = ((maxMemoryBytes / 1000) / 1000);
        long maxMemGb = ((maxMemMb / 1000));
        assertThat(maxMemGb).isGreaterThanOrEqualTo(16);
    }

    private static final File FILE;

    static
    {
        try {
            FILE = File.createTempFile("data", null);
            new CSVGenerator(50, FILE).go();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final VarHandle BYTE_HANDLE = tryCreateByteHandler();
    private static VarHandle tryCreateByteHandler() {
        try {
            return MethodHandles.arrayElementVarHandle(byte[].class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize VarHandles", e);
        }
    }
}
