package education.jackson.versioning.casestudy1;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(value = {"address"})
public abstract class UpsertCustomerMixInVersion2
{
    @JsonCreator()
    UpsertCustomerMixInVersion2(
            @JsonProperty(value = "customerId", required = true) final String customerId,
            @JsonProperty(value = "source", required = true) final String source,
            @JsonProperty(value = "firstName", required = true) final String firstName,
            @JsonProperty(value = "secondName", required = true) final String secondName)
    {
    }
}
