package education.jackson.versioning.casestudy1.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"type"})
public interface Request
{
    String REMOVED = null;

    String getType();
}
