package education.jackson.versioning.casestudyByBuilder.requests;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import education.jackson.versioning.casestudyByBuilder.converters.AnotherRequestBuilder;
import education.jackson.versioning.casestudyByBuilder.converters.UpsertCustomerBuilder;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpsertCustomerBuilder.class, name = "UpsertCustomer"),
        @JsonSubTypes.Type(value = AnotherRequestBuilder.class, name = "AnotherRequest"),
})
public interface Request
{
    default String getType()
    {
        return getClass().getSimpleName();
    }
}
