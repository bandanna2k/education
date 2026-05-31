package education.openapi.specfirst.operations;

class InsufficientFundsException extends RuntimeException
{
    InsufficientFundsException(String message)
    {
        super(message);
    }
}
