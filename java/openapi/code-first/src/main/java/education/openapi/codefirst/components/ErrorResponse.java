package education.openapi.codefirst.components;


public class ErrorResponse   {
  public String code;
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

