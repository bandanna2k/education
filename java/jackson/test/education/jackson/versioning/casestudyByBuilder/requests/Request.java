package education.jackson.versioning.casestudyByBuilder.requests;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import education.jackson.versioning.casestudyByBuilder.pojos.Builder;
import education.jackson.versioning.casestudyByBuilder.pojos.UpsertCustomer;
import education.jackson.versioning.casestudyByBuilder.pojos.UpsertCustomerBuilder;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpsertCustomerBuilder.class, name = "UpsertCustomer"),
})
public interface Request
{
    default String getType()
    {
        return getClass().getSimpleName();
    }
}
