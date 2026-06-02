package education.openapi.codefirst.operations;

public enum ApiError
{
    INSUFFICIENT_FUNDS(400, "Insufficient funds.");

    public final int errorCode;
    public final String errorMessage;

    ApiError(int errorCode, String errorMessage)
    {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
