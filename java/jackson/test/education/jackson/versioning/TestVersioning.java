package education.jackson.versioning;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class TestVersioning
{
    private static final String JSON_WITH_NAME = """
            {
                "type": "UpsertCustomer",
                "customerId": "67",
                "source": "FIAT",
                "name": "Tom SAWYER"
            }
            """;
    private static final String JSON_WITH_FIRST_AND_SECOND_NAME = """
            {
                "type": "UpsertCustomer",
                "customerId": "67",
                "source": "FIAT",
                "firstName": "Tom",
                "secondName": "SAWYER"
            }
            """;

    public static class ConverterVersion1 extends Module
    {
        @Override
        public String getModuleName() {
            return this.getClass().getSimpleName();
        }
        @Override
        public Version version()
        {
            return new Version(1, 0, 0, null, null, null);
        }
        @Override
        public void setupModule(SetupContext setupContext)
        {
            setupContext.setMixInAnnotations(UpsertCustomer.class, UpsertCustomerMixIn.class);
        }
    }

    public static class ConverterVersion2 extends Module
    {
        @Override
        public String getModuleName() {
            return this.getClass().getSimpleName();
        }
        @Override
        public Version version()
        {
            return new Version(2, 0, 0, null, null, null);
        }
        @Override
        public void setupModule(SetupContext setupContext)
        {
            setupContext.setMixInAnnotations(UpsertCustomer.class, UpsertCustomerMixInVersion2.class);
        }
    }

    @JsonIgnoreProperties({"type"})
    public interface Request
    {
        String getType();
    }

    public static class UpsertCustomer implements Request
    {
        public String customerId;
        public String source;
        public String firstName;
        public String secondName;

        @Override
        public String getType() {
            return this.getClass().getSimpleName();
        }

        public void setName(String name)
        {
            int indexOf = name.indexOf(" ");
            if(indexOf < 0)
            {
                this.firstName = name;
            }
            else
            {
                this.firstName = name.substring(0, indexOf);
                this.secondName = name.substring(indexOf + 1);
            }
        }

        @Override
        public String toString()
        {
            return "UpsertCustomer{" +
                    "customerId='" + customerId + '\'' +
                    ", source='" + source + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", secondName='" + secondName + '\'' +
                    '}';
        }
    }

    public abstract static class UpsertCustomerMixIn
    {
        @JsonCreator
        UpsertCustomerMixIn(
                @JsonProperty("customerId") final String customerId,
                @JsonProperty("source") final String source,
                @JsonProperty("name") final String name)
        {
        }
    }

    public abstract static class UpsertCustomerMixInVersion2
    {
        @JsonCreator
        UpsertCustomerMixInVersion2(
                @JsonProperty("customerId") final String customerId,
                @JsonProperty("source") final String source,
                @JsonProperty("firstName") final String firstName,
                @JsonProperty("secondName") final String secondName)
        {
        }
    }

    @Test
    public void testVersion1() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .registerModule(new ConverterVersion1());
        {
            UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_NAME, UpsertCustomer.class);
            assertThat(upsertCustomer.firstName).isEqualTo("Tom");
        }
        {
            UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME, UpsertCustomer.class);
            assertThat(upsertCustomer.firstName).isEqualTo("Tom");
        }
    }

    @Test
    public void testVersion2() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .registerModule(new ConverterVersion2());
        {
            UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_NAME, UpsertCustomer.class);
            assertThat(upsertCustomer.firstName).isEqualTo("Tom");
        }
        {
            UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME, UpsertCustomer.class);
            assertThat(upsertCustomer.firstName).isEqualTo("Tom");
        }
    }
}
