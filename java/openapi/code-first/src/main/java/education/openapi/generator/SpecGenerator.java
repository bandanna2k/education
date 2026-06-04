package education.openapi.generator;

import education.openapi.operations.BalanceOperation;
import education.openapi.operations.DepositOperation;
import education.openapi.operations.WithdrawalOperation;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SpecGenerator
{
    private static final String GENERATED_FOLDER = "generated";
    private static final String SPEC_FILE_NAME = "spec.yaml";

    public OpenAPI generate()
    {
        try {
            Set<Class<?>> resourceClasses = new HashSet<>(Arrays.asList(
                    Class.forName(BalanceOperation.class.getCanonicalName()),
                    Class.forName(DepositOperation.class.getCanonicalName()),
                    Class.forName(WithdrawalOperation.class.getCanonicalName())
            ));

            // Create OpenAPI configuration
            Set<String> resourceClassStrings = new HashSet<>(Arrays.asList(
                    BalanceOperation.class.getCanonicalName(),
                    DepositOperation.class.getCanonicalName(),
                    WithdrawalOperation.class.getCanonicalName()
            ));

            SwaggerConfiguration config = new SwaggerConfiguration()
                    .resourceClasses(resourceClassStrings);

            // Generate OpenAPI spec
            Reader reader = new Reader(config);
            OpenAPI openAPI = reader.read(resourceClasses);

            // Remove RoutingContext component if present (picked up from handle() method signature)
            if (openAPI.getComponents() != null && openAPI.getComponents().getSchemas() != null) {
                openAPI.getComponents().getSchemas().remove("RoutingContext");
            }

            // Add info if not present
            if (openAPI.getInfo() == null) {
                openAPI.setInfo(new Info()
                        .title("Account Management API")
                        .version("1.0.0")
                        .description("API for managing account deposits, withdrawals, and balance inquiries"));
            }

            // Add server info
            Server server = new Server();
            server.setUrl("http://localhost:8080");
            server.setDescription("Development server");
            openAPI.servers(Arrays.asList(server));

            // Write to spec file
            writeSpecFile(openAPI);

            return openAPI;
        } catch (Exception e) {
            System.err.println("Failed to generate OpenAPI spec: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void writeSpecFile(OpenAPI openAPI) throws IOException {
        // Create generated folder next to src/main and src/test in code-first module.
        Path generatedPath = resolveGeneratedPath();
        Files.createDirectories(generatedPath);

        // Write spec.yaml file
        Path specFilePath = generatedPath.resolve(SPEC_FILE_NAME);
        String yamlContent = Yaml.pretty(openAPI);

        try (FileWriter writer = new FileWriter(specFilePath.toFile())) {
            writer.write(yamlContent);
        }

        System.out.println("Generated spec file: " + specFilePath.toAbsolutePath());
    }

    private Path resolveGeneratedPath()
    {
        Path cwd = Paths.get("").toAbsolutePath();
        Path moduleSrc = cwd.resolve("src");

        if (Files.isDirectory(moduleSrc.resolve("main")) && Files.isDirectory(moduleSrc.resolve("test"))) {
            return moduleSrc.resolve(GENERATED_FOLDER);
        }

        Path workspaceModuleSrc = cwd.resolve("java/openapi/code-first/src");
        if (Files.isDirectory(workspaceModuleSrc.resolve("main")) && Files.isDirectory(workspaceModuleSrc.resolve("test"))) {
            return workspaceModuleSrc.resolve(GENERATED_FOLDER);
        }

        return moduleSrc.resolve(GENERATED_FOLDER);
    }

    public static void main(String[] args) {
        SpecGenerator generator = new SpecGenerator();
        OpenAPI spec = generator.generate();
        if (spec != null) {
            System.out.println("OpenAPI spec generated successfully!");
        } else {
            System.err.println("Failed to generate OpenAPI spec");
        }
    }
}

