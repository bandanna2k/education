package education.onebrc.memorymapping;

import java.io.*;
import java.util.Random;

public class CSVGenerator {
    private static final String[] FIRST_NAMES = {
            "James", "Mary", "Robert", "Patricia", "Michael", "Jennifer", "William", "Linda",
            "David", "Barbara", "Richard", "Elizabeth", "Joseph", "Susan", "Thomas", "Jessica",
            "Charles", "Sarah", "Christopher", "Karen", "Daniel", "Nancy", "Matthew", "Lisa",
            "Anthony", "Betty", "Mark", "Margaret", "Donald", "Sandra", "Steven", "Ashley",
            "Paul", "Kimberly", "Andrew", "Donna", "Joshua", "Carol", "Kenneth", "Michelle"
    };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Miller", "Davis", "Rodriguez",
            "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor",
            "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White", "Harris",
            "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Young", "Walker", "Allen"
    };

    private static final int BUFFER_SIZE = 1_000_000; // 1MB buffer
    private final Random random = new Random(1);
    private final int targetSize;
    private final File file;

    public CSVGenerator() throws IOException {
        this(1000, File.createTempFile(null, null));
    }
    public CSVGenerator(int countOf1MbBuffers, File file) {
        this.targetSize = countOf1MbBuffers * BUFFER_SIZE;
        this.file = file;
    }

    public void go() throws IOException
    {
        System.out.println("Generating file...");
        System.out.println("Output file: " + file);
        System.out.println();

        long startTime = System.currentTimeMillis();
        long bytesWritten = generateCSV(file);
        long endTime = System.currentTimeMillis();

        System.out.println("? File generation complete!");
        System.out.println("Bytes written: " + bytesWritten + " (" + (bytesWritten / 1_000_000_000.0) + " GB)");
        System.out.println("Time taken: " + ((endTime - startTime) / 1000.0) + " seconds");
    }

    private long generateCSV(File file) throws IOException {
        long bytesWritten = 0;
        StringBuilder buffer = new StringBuilder(BUFFER_SIZE);

        try (FileWriter writer = new FileWriter(file)) {
            // Write header
            String header = "Name,5K_Time_Seconds\n";
            writer.write(header);
            bytesWritten += header.getBytes().length;

            // Generate data until we reach ~1GB
            while (bytesWritten < targetSize) {
                String name = generateRandomName();
                int timeInSeconds = generateRandomTime();
                String line = name + "," + timeInSeconds + "\n";

                buffer.append(line);
                bytesWritten += line.getBytes().length;

                // Flush buffer periodically
                if (buffer.length() > BUFFER_SIZE) {
                    writer.append(buffer);
                    buffer = new StringBuilder(BUFFER_SIZE);
                }

                // Progress indicator
                if (bytesWritten % 100_000_000 == 0) {
                    System.out.printf("Progress: %.1f GB written%n", bytesWritten / 1_000_000_000.0);
                }
            }

            // Flush remaining buffer
            if (!buffer.isEmpty()) {
                writer.append(buffer);
            }
        }

        return bytesWritten;
    }

    private String generateRandomName() {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        return firstName + " " + lastName;
    }

    private int generateRandomTime() {
        // 17 minutes = 1020 seconds
        // 59 minutes = 3540 seconds
        return 1020 + random.nextInt(2521); // 2521 = 3540 - 1020 + 1
    }
}