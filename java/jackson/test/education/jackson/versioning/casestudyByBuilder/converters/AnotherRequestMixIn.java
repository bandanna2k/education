package education.jackson.versioning.casestudyByBuilder.converters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AnotherRequestMixIn
{
    @JsonCreator()
    AnotherRequestMixIn()
    {
    }
}
