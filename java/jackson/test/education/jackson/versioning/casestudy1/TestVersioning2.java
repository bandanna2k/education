package education.jackson.versioning.casestudy1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import education.jackson.versioning.casestudy1.requests.UpsertCustomer;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TestVersioning2
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
    private static final String JSON_WITH_ADDRESS_V3 = """
            {
                "type": "UpsertCustomer",
                "customerId": "67",
                "source": "FIAT",
                "firstName": "Tom",
                "secondName": "SAWYER",
                "address": "999 Letsbe Avenue"
            }
            """;

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
    public abstract static class UpsertCustomerMixInVersion3
    {
        @JsonCreator
        UpsertCustomerMixInVersion3(
                @JsonProperty(value = "customerId", required = true) final String customerId,
                @JsonProperty(value = "source", required = true) final String source,
                @JsonProperty(value = "firstName", required = true) final String firstName,
                @JsonProperty(value = "secondName", required = true) final String secondName,
                @JsonProperty(value = "address") final String address)
        {
        }
    }

    @Test
    public void givenVersion1ProtocolVersion1Works() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JsonModuleVersion1());
        UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_NAME_V1, UpsertCustomer.class);
        assertThat(upsertCustomer.firstName).isEqualTo("Tom");
    }
    @Test
    public void givenVersion1ProtocolVersion2DoesNotWork() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JsonModuleVersion1());
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, UpsertCustomer.class));
    }
    @Test
    public void givenVersion1ProtocolVersion3DoesNotWork() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JsonModuleVersion1());
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, UpsertCustomer.class));
    }


    @Test
    public void givenVersion2ProtocolVersion1DoesNotWork() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JsonModuleVersion2());
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> mapper.readValue(JSON_WITH_NAME_V1, UpsertCustomer.class));
    }
    @Test
    public void givenVersion2ProtocolVersion2Works() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JsonModuleVersion2());

        UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, UpsertCustomer.class);
        assertThat(upsertCustomer.firstName).isEqualTo("Tom");
    }
    @Test
    public void givenVersion2ProtocolVersion3DoesNotWork() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JsonModuleVersion2());
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> mapper.readValue(JSON_WITH_NAME_V1, UpsertCustomer.class));
    }


    @Test
    public void givenVersion3Protocol_RequestVersion1DoesNotWork() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JsonModuleVersion3());
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> mapper.readValue(JSON_WITH_NAME_V1, UpsertCustomer.class));
    }
    @Test
    public void givenVersion3Protocol_RequestVersion2Works() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JsonModuleVersion3());
        UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, UpsertCustomer.class);
        assertThat(upsertCustomer.address).isEqualTo(null);
    }
    @Test
    public void givenVersion3Protocol_RequestVersion3Works() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JsonModuleVersion3());
        UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_ADDRESS_V3, UpsertCustomer.class);
        assertThat(upsertCustomer.address).isEqualTo("999 Letsbe Avenue");
    }
}
