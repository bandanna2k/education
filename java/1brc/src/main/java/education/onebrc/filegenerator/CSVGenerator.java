package education.onebrc.filegenerator;

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

    private static final long TARGET_SIZE = 1_000_000_000L; // 1GB in bytes
    private static final int BUFFER_SIZE = 1_000_000; // 1MB buffer
    private static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        String filename = "/home/northd/data.csv";
        System.out.println("Generating 1GB CSV file...");
        System.out.println("Output file: " + filename);
        System.out.println();

        long startTime = System.currentTimeMillis();
        long bytesWritten = generateCSV(filename);
        long endTime = System.currentTimeMillis();

        System.out.println("? File generation complete!");
        System.out.println("Bytes written: " + bytesWritten + " (" + (bytesWritten / 1_000_000_000.0) + " GB)");
        System.out.println("Time taken: " + ((endTime - startTime) / 1000.0) + " seconds");
    }

    private static long generateCSV(String filename) throws IOException {
        long bytesWritten = 0;
        StringBuilder buffer = new StringBuilder(BUFFER_SIZE);

        try (FileWriter writer = new FileWriter(filename)) {
            // Write header
            String header = "Name,5K_Time_Seconds\n";
            writer.write(header);
            bytesWritten += header.getBytes().length;

            // Generate data until we reach ~1GB
            while (bytesWritten < TARGET_SIZE) {
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
            if (buffer.length() > 0) {
                writer.append(buffer);
            }
        }

        return bytesWritten;
    }

    private static String generateRandomName() {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        return firstName + " " + lastName;
    }

    private static int generateRandomTime() {
        // 17 minutes = 1020 seconds
        // 59 minutes = 3540 seconds
        return 1020 + random.nextInt(2521); // 2521 = 3540 - 1020 + 1
    }
}