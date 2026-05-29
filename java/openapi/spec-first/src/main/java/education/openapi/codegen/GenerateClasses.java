package education.openapi.codegen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

public class GenerateClasses {

    public static void main(String[] args) {
        new GenerateClasses().go();
    }

    public void go() {
        Path projectDir = Paths.get("").toAbsolutePath();
        Path cliJar = projectDir.resolve("swagger-codegen-cli.jar");
        Path spec = projectDir.resolve("src/main/resources/spec.yaml");
        Path outputDir = projectDir.resolve("generated");

        if (!Files.exists(cliJar)) {
            throw new IllegalStateException("Missing swagger-codegen-cli.jar at " + cliJar);
        }
        if (!Files.exists(spec)) {
            throw new IllegalStateException("Missing spec file at " + spec);
        }

        deleteDirectory(outputDir);

        List<String> command = List.of(
                "java", "-jar", cliJar.toString(),
                "generate",
                "-i", spec.toString(),
                "-l", "java",
                "-o", outputDir.toString()
        );

        try {
            Process process = new ProcessBuilder(command)
                    .directory(projectDir.toFile())
                    .inheritIO()
                    .start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IllegalStateException("swagger-codegen failed with exit code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("Unable to run swagger-codegen", e);
        }

        cleanUnusedGeneratedFiles(outputDir);

        System.out.println("Generated stubs and schema in " + outputDir);
    }

    private void cleanUnusedGeneratedFiles(Path outputDir) {
        deleteDirectory(outputDir.resolve("docs"));
        deleteDirectory(outputDir.resolve("src/test"));
        deleteDirectory(outputDir.resolve("gradle"));
        deleteDirectory(outputDir.resolve(".swagger-codegen"));

        deleteFile(outputDir.resolve(".swagger-codegen-ignore"));
        deleteFile(outputDir.resolve(".gitignore"));
        deleteFile(outputDir.resolve(".travis.yml"));
        deleteFile(outputDir.resolve("README.md"));
        deleteFile(outputDir.resolve("build.gradle"));
        deleteFile(outputDir.resolve("build.sbt"));
        deleteFile(outputDir.resolve("settings.gradle"));
        deleteFile(outputDir.resolve("gradle.properties"));
        deleteFile(outputDir.resolve("pom.xml"));
        deleteFile(outputDir.resolve("gradlew"));
        deleteFile(outputDir.resolve("gradlew.bat"));
        deleteFile(outputDir.resolve("git_push.sh"));
        deleteFile(outputDir.resolve("src/main/AndroidManifest.xml"));
    }

    private void deleteFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to delete " + path, e);
        }
    }

    private void deleteDirectory(Path directory) {
        if (!Files.exists(directory)) {
            return;
        }
        try (var walk = Files.walk(directory)) {
            walk.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    throw new IllegalStateException("Unable to delete " + path, e);
                }
            });
        } catch (IOException e) {
            throw new IllegalStateException("Unable to clean output directory " + directory, e);
        }
    }
}
