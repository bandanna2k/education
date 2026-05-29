# OpenAPI Specification Generation

This module uses code-first development with OpenAPI annotations to automatically generate an OpenAPI specification document.

## Overview

The specification is generated from Swagger/OpenAPI v3 annotations on the JAX-RS endpoint interfaces and model classes. The generated `spec.yaml` file is placed in the `src/generated/` directory.

## Files Involved

### Endpoints
- `src/main/java/education/openapi/codefirst/endpoints/BalanceApi.java` - Get account balance endpoint
- `src/main/java/education/openapi/codefirst/endpoints/DepositApi.java` - Deposit funds endpoint
- `src/main/java/education/openapi/codefirst/endpoints/WithdrawalApi.java` - Withdraw funds endpoint

### Components (Models)
- `src/main/java/education/openapi/codefirst/components/Balance.java` - Balance response model
- `src/main/java/education/openapi/codefirst/components/AccountRequest.java` - Account request model
- `src/main/java/education/openapi/codefirst/components/TransactionRequest.java` - Transaction request model
- `src/main/java/education/openapi/codefirst/components/ErrorResponse.java` - Error response model

### Generator
- `src/main/java/education/openapi/codefirst/SpecGenerator.java` - Main class that generates the spec

## Annotations Used

The following OpenAPI v3 annotations are used:

- `@Tag` - Define operation tags on endpoint classes
- `@Operation` - Describe individual operations
- `@RequestBody` - Document request body with schema
- `@ApiResponse` - Document response with status code and schema
- `@Schema` - Define schema for model classes and properties

## Generating the Specification

### Option 1: Using Gradle Task
```bash
./gradlew generateSpec
```

This will:
1. Compile the code
2. Run the SpecGenerator class
3. Generate `src/generated/spec.yaml`

### Option 2: Running SpecGenerator Directly
```bash
./gradlew run --class education.openapi.codefirst.SpecGenerator
```

## Output

The generated `spec.yaml` file contains:
- API information (title, version, description)
- Server configuration
- Tags for logical grouping
- Paths with operations (GET, POST)
- Component schemas for request/response models
- Complete documentation for each endpoint

Example output structure:
```yaml
openapi: 3.0.1
info:
  title: Account Management API
  version: 1.0.0
paths:
  /balance:
    get:
      # Operation details
  /deposit:
    post:
      # Operation details
  /withdrawal:
    post:
      # Operation details
components:
  schemas:
    Balance: {}
    AccountRequest: {}
    TransactionRequest: {}
    ErrorResponse: {}
```

## Key Features

- **Automatic schema generation**: Models are automatically converted to OpenAPI schemas
- **Type-safe**: Uses JAX-RS annotations for routing and OpenAPI annotations for documentation
- **Validation annotations**: Supports `@Valid` and `@NotNull` from javax.validation
- **Example values**: Includes example values in schema definitions
- **Request/response documentation**: Clear documentation of what each endpoint expects and returns

## Dependencies

The following dependencies are required for spec generation:
- `io.swagger.core.v3:swagger-core:2.2.21`
- `io.swagger.core.v3:swagger-annotations:2.2.21`
- `io.swagger.core.v3:swagger-models:2.2.21`
- `io.swagger.core.v3:swagger-jaxrs2:2.2.21`

## Integration

The generated `spec.yaml` can be used with:
- SwaggerUI for interactive API documentation
- OpenAPI code generators to create client libraries
- API documentation tools
- API testing tools (Postman, Insomnia, etc.)

## Notes

- The `src/generated/` folder structure mirrors `src/main/` and `src/test/`
- The specification is generated at build time and included in the JAR
- To update the spec, simply modify the annotations and regenerate
- The application code doesn't depend on the generated spec; it's purely for documentation

