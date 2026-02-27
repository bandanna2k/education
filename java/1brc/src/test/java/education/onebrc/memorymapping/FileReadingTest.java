package education.onebrc.memorymapping;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FileReadingTest {

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

    @Test
    void read1() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;

            List<String> mostRecent = null;
            while ((line = br.readLine()) != null) {
                mostRecent = parseCSVLine(line);
            }
            System.out.println(mostRecent);
        }
    }

    @Test
    void read2WithMemoryMapping() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(FILE, "r");
             FileChannel channel = file.getChannel()) {

            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            String content = StandardCharsets.UTF_8.decode(buffer).toString();
            String[] lines = content.split("\n");

            List<String> mostRecent = null;
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    mostRecent = parseCSVLine(line);
                }
            }
            System.out.println(mostRecent);
        }
    }

    private static List<String> parseCSVLine(String line) {
        String[] parts = line.split(",", -1);
        List<String> values = new ArrayList<>();
        for (String part : parts) {
            values.add(part.trim());
        }
        return values;
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
        new CSVGenerator().go();
    }
}
