package education.jackson.versioning.casestudyByBuilder.converters;

import education.jackson.versioning.casestudyByBuilder.pojos.AnotherRequest;

public class AnotherRequestBuilder implements Builder<AnotherRequest>, Request
{
    private final int version;

    public AnotherRequestBuilder()
    {
        version = 1;
    }

    @Override
    public AnotherRequest build()
    {
        return switch (version) {
            case 1 -> new AnotherRequest();
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }
}
