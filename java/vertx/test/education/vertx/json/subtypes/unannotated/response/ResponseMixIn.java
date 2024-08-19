package education.vertx.json.subtypes.unannotated.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "ResponseMixIn")
public class ResponseMixIn
{
    @JsonProperty(value =  "type")
    private String type;
}
