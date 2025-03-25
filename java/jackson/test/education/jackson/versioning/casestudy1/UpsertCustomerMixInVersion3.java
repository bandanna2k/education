package education.jackson.versioning.casestudy1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class UpsertCustomerMixInVersion3
{

    public static final String NULL = null;

    @JsonCreator()
    UpsertCustomerMixInVersion3(
            @JsonProperty(value = "customerId", required = true) final String customerId,
            @JsonProperty(value = "source", required = true) final String source,
            @JsonProperty(value = "firstName", required = true) final String firstName,
            @JsonProperty(value = "secondName", required = true) final String secondName,
            @JsonProperty(value = "address") final String address)
    {
    }
}
