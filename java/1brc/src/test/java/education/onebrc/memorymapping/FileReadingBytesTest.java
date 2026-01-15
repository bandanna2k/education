package education.onebrc.memorymapping;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static org.assertj.core.api.Assertions.assertThat;

public class FileReadingBytesTest {

    private static final String FILENAME = "/home/northd/data10mb.csv";

    @Test
    void read1() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(FILENAME, "r")) {
            int sum = 0;
            int input;
            while((input = file.read()) != -1) {
                sum += input;
            }
            System.out.println(sum);
        }
    }

    @Test
    void read2WithMemoryMapping() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(FILENAME, "r");
             FileChannel channel = file.getChannel()) {

            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            int sum = 0;
            while (buffer.hasRemaining()) {
                sum += (buffer.get() & 0xFF);
            }
            System.out.println(sum);
        }
    }

    @BeforeAll
    public static void needsXmx16g()
    {
        long maxMemoryBytes = Runtime.getRuntime().maxMemory();
        long maxMemMb = ((maxMemoryBytes / 1000) / 1000);
        long maxMemGb = ((maxMemMb / 1000));
        assertThat(maxMemGb).isGreaterThanOrEqualTo(16);
    }

    @BeforeAll
    public static void writeFile() throws IOException
    {
        new CSVGenerator(10, FILENAME).go();
    }
}
