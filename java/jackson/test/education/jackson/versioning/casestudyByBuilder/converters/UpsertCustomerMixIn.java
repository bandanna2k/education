package education.jackson.versioning.casestudyByBuilder.converters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class UpsertCustomerMixIn
{
    @JsonCreator()
    UpsertCustomerMixIn(
            @JsonProperty(value = "customerId", required = true) final String customerId,
            @JsonProperty(value = "source", required = true) final String source,
            @JsonProperty(value = "name", required = true) final String name)
    {
    }
}
