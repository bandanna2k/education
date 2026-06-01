package education.openapi.codefirst.operations.components;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "ErrorResponse",
    description = "Error response object"
)
public class ErrorResponse   {
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

  public ErrorResponse()
  {
  }

  public ErrorResponse(String code, String message)
  {
    this.code = code;
    this.message = message;
  }
}

