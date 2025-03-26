package education.jackson.versioning.casestudyByBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class UpsertCustomerMixIn_V3
{
    @JsonCreator()
    UpsertCustomerMixIn_V3(
            @JsonProperty(value = "customerId", required = true) final String customerId,
            @JsonProperty(value = "source", required = true) final String source,
            @JsonProperty(value = "firstName", required = true) final String firstName,
            @JsonProperty(value = "secondName", required = true) final String secondName,
            @JsonProperty(value = "address", required = true) final String address)
    {
    }
}
