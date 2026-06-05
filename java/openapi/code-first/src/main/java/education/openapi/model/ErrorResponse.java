package education.openapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ErrorResponse",
        description = "Error response object"
)
public class ErrorResponse {
    @Schema(
            description = "Error code identifier",
            example = "BAD_REQUEST"
    )
    public String code;

    @Schema(
            description = "Human-readable error message",
            example = "Invalid request parameters"
    )
    public String message;

    public ErrorResponse() {
    }

    public ErrorResponse code(String code) {
        this.code = code;
        return this;
    }

    public ErrorResponse message(String message) {
        this.message = message;
        return this;
    }
}

