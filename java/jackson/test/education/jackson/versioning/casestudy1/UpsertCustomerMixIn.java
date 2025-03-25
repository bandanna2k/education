package education.jackson.versioning.casestudy1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
