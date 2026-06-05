package education.openapi.application;

public enum ApiError
{
    INSUFFICIENT_FUNDS(400, "Insufficient funds."),
    BAD_REQUEST(400, "Bad request.");

    public final int errorCode;
    public final String errorMessage;

    ApiError(int errorCode, String errorMessage)
    {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
