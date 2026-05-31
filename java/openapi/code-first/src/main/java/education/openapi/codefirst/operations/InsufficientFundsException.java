package education.openapi.codefirst.operations;

class InsufficientFundsException extends RuntimeException
{
    InsufficientFundsException(String message)
    {
        super(message);
    }
}
