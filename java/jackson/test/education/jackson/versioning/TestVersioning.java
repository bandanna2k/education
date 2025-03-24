package education.jackson.versioning;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class TestVersioning
{
    private static final String JSON_WITH_NAME_V1 = """
            {
                "type": "UpsertCustomer",
                "customerId": "67",
                "source": "FIAT",
                "name": "Tom SAWYER"
            }
            """;
    private static final String JSON_WITH_FIRST_AND_SECOND_NAME_V2 = """
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

    @JsonIgnoreProperties(value = {"type"})
    public interface Request
    {
        String getType();
    }

    public static class UpsertCustomer implements Request
    {
        private final String customerId;
        private final String source;
        private final String firstName;
        private final String secondName;

        public UpsertCustomer(String customerId, String source, String name)    // Input version 1, Output version 2
        {
            this.customerId = customerId;
            this.source = source;

            {   // Upgrade name to first name and last name
                int indexOf = name.indexOf(" ");
                if (indexOf < 0)
                {
                    this.firstName = name;
                    this.secondName = null;
                }
                else
                {
                    this.firstName = name.substring(0, indexOf);
                    this.secondName = name.substring(indexOf + 1);
                }
            }
        }

        public UpsertCustomer(String customerId, String source, String firstName, String secondName)    // Input version 2, output version 2
        {
            this.customerId = customerId;
            this.source = source;
            this.firstName = firstName;
            this.secondName = secondName;
        }

        @Override
        public String getType() {
            return this.getClass().getSimpleName();
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
                @JsonProperty(value = "customerId", required = true) final String customerId,
                @JsonProperty(value = "source", required = true) final String source,
                @JsonProperty(value = "name", required = true) final String name)
        {
        }
    }

    public abstract static class UpsertCustomerMixInVersion2
    {
        @JsonCreator
        UpsertCustomerMixInVersion2(
                @JsonProperty(value = "customerId", required = true) final String customerId,
                @JsonProperty(value = "source", required = true) final String source,
                @JsonProperty(value = "firstName", required = true) final String firstName,
                @JsonProperty(value = "secondName", required = true) final String secondName)
        {
        }
    }

    @Test
    public void givenVersion1ProtocolVersion1Works() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ConverterVersion1());
        UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_NAME_V1, UpsertCustomer.class);
        assertThat(upsertCustomer.firstName).isEqualTo("Tom");
    }
    @Test
    public void givenVersion1ProtocolVersion2DoesNotWork() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ConverterVersion1());
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, UpsertCustomer.class));
    }

    @Test
    public void givenVersion2ProtocolVersion1DoesNotWork() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ConverterVersion2());
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> mapper.readValue(JSON_WITH_NAME_V1, UpsertCustomer.class));
    }
    @Test
    public void givenVersion2ProtocolVersion2Works() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ConverterVersion2());

        UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, UpsertCustomer.class);
        assertThat(upsertCustomer.firstName).isEqualTo("Tom");
    }
}
